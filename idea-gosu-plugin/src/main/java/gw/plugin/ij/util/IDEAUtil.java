/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.base.Function;
import com.google.common.collect.AbstractLinkedIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.intellij.diagnostic.DefaultIdeaErrorLogger;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.ErrorLogger;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffPanel;
import com.intellij.openapi.diff.SimpleContent;
import com.intellij.openapi.diff.SimpleDiffRequest;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.PsiAwareFileEditorManagerImpl;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.impl.ModuleManagerImpl;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.indexing.IndexingDataKeys;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.LocationInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.core.IJModuleNode;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.core.UnidirectionalCyclicGraph;
import gw.plugin.ij.filesystem.IDEADirectory;
import gw.plugin.ij.filesystem.IDEAFile;
import gw.plugin.ij.filesystem.IDEAFileSystem;
import gw.plugin.ij.filesystem.IDEAResource;
import gw.plugin.ij.filetypes.GosuFileTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.lang.psi.impl.ModuleFileContext;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;


public class IDEAUtil {
  private static final Logger LOG = Logger.getInstance(IDEAUtil.class);
  private static final ErrorLogger ERROR_LOGGER = new DefaultIdeaErrorLogger();

  public static boolean _inTestMode = false;

  public static final String GOSU_NOTIFICATION_GROUP = "GosuGroup";
  public static final String MAGIC_INJECTED_SUFFIX = "_INJECTED_"; // TODO: implement in a general way

  public static final String JAR_INDICATOR = ".jar!";

  public static final PluginId EDITOR_PLUGIN_ID = PluginId.getId("com.guidewire.gosu");
  public static final PluginId OLD_EDITOR_PLUGIN_ID = PluginId.getId("Gosu");

  public static List<Module> getDependencies(@NotNull Module module) {
    return ImmutableList.copyOf(ModuleRootManager.getInstance(module).getDependencies());
  }

  public static IdeaPluginDescriptor getEditorPlugin() {
    final IdeaPluginDescriptor plugin = PluginManager.getPlugin(EDITOR_PLUGIN_ID);
    return plugin != null ? plugin : PluginManager.getPlugin(OLD_EDITOR_PLUGIN_ID);
  }

  @Nullable
  public static String getCircularModuleDependency(@NotNull Project project) {
    final UnidirectionalCyclicGraph<Module> graph = new UnidirectionalCyclicGraph<>();
    final ModuleManagerImpl instance = (ModuleManagerImpl) ModuleManagerImpl.getInstance(project);
    for (Module module : instance.getModules()) {
      final IJModuleNode node = new IJModuleNode(module);
      graph.registerNode(node.getId(), node);
    }
    try {
      graph.resolveLinks();
      return null;
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public static <R> R runInModule(@NotNull Callable<R> callback, @NotNull PsiElement context) {
    final IModule module = GosuModuleUtil.findModuleForPsiElement(context);
    TypeSystem.pushModule(module);
    try {
      return callback.call();
    } catch (ProcessCanceledException e) {
      throw e;
    } catch (Exception e) {
      if (ExceptionUtil.isWrappedCanceled(e)) {
        throw new ProcessCanceledException(e.getCause());
      } else {
        throw new RuntimeException(e);
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }

  public static PsiFile[] getFiles(@NotNull final PsiDirectory directory) {
    return ApplicationManager.getApplication().runReadAction(new Computable<PsiFile[]>() {
      @NotNull
      public PsiFile[] compute() {
        return directory.getFiles();
      }
    });
  }

  @Nullable
  public static VirtualFile getFileFromPsi(@NotNull PsiFile file) {
    VirtualFile vfile = file.getUserData(IndexingDataKeys.VIRTUAL_FILE);
    if (vfile == null) {
      vfile = file.getVirtualFile();
      if (vfile == null) {
        vfile = file.getOriginalFile().getVirtualFile();
        if (vfile == null) {
          vfile = file.getViewProvider().getVirtualFile();
        }
      } else if (vfile instanceof LightVirtualFile) {
        PsiFile containingFile = file.getContainingFile();
        if (containingFile != null && containingFile != file) {
          PsiFile originalFile = containingFile.getOriginalFile();
          SmartPsiElementPointer owningFile = originalFile.getUserData(FileContextUtil.INJECTED_IN_ELEMENT);
          if (owningFile != null) {
            vfile = owningFile.getVirtualFile();
          }
        }
      }
    }
    return vfile;
  }

  public static void runInDispatchThread(@NotNull Runnable operation, boolean blocking) {
    Application application = ApplicationManager.getApplication();
    if (application.isDispatchThread()) {
      operation.run();
    } else if (blocking) {
      application.invokeAndWait(operation, ModalityState.defaultModalityState());
    } else {
      application.invokeLater(operation);
    }
  }

  public static void runWriteActionInDispatchThread(@NotNull final Runnable operation, boolean blocking) {
    final Application application = ApplicationManager.getApplication();

    final Runnable action = new Runnable() {
      public void run() {
        application.runWriteAction(operation);
      }
    };

    if (application.isDispatchThread()) {
      action.run();
    } else if (blocking) {
      application.invokeAndWait(action, ModalityState.defaultModalityState());
    } else {
      application.invokeLater(action);
    }
  }

  public static void runWithTiming(String description, double thresholdSecs, @NotNull Runnable runnable) {
    long start = System.nanoTime();
    try {
      runnable.run();
    } finally {
      printTiming(description, thresholdSecs, start);
    }
  }

  public static void printTiming(String description, double thresholdSecs, long start) {
    final double dt = (System.nanoTime() - start) * 1e-9;
    if (dt >= thresholdSecs) {
      System.out.printf(description + " done in %.3fs.\n", dt);
    }
  }

  public static VirtualFile getOriginalFile(VirtualFileWindow window) {
    VirtualFile file = window.getDelegate();
    if (file instanceof LightVirtualFile) {
      final VirtualFile original = ((LightVirtualFile) file).getOriginalFile();
      if (original != null) {
        file = original;
      }
    }
    return file;
  }

  public static String getSourceQualifiedName(@NotNull VirtualFile file, @NotNull IModule module) {
    if (file.getName().equals(GosuPsiParseUtil.TRANSIENT_PROGRAM)) {
      return "transient";
    }

    if (file.getName().contains(GosuScratchpadFileImpl.GOSU_SCRATCHPAD_NAME)) {
      return GosuScratchpadFileImpl.FQN;
    }

    if (file instanceof VirtualFileWindow) {
      final VirtualFileWindow window = (VirtualFileWindow) file;
      final VirtualFile original = getOriginalFile(window);
      final List<String> typeNames = IDEAUtil.getTypesForFile(module, original);
      if (typeNames.isEmpty()) {
        LOG.warn(String.format("No types for '%s', original is '%s'", file, original));
        return "unable.to.find.name";
      } else {
        return typeNames.get(0) + IDEAUtil.getInjectedFileSuffix(window, module);
      }
    }

    if (file instanceof LightVirtualFile) {
      return file.getNameWithoutExtension().replace(File.separatorChar, '.');
    }

    // General case
    final List<String> typeNames = IDEAUtil.getTypesForFile(module, file);
    if (typeNames.isEmpty()) {
      LOG.warn(String.format("File '%s' is outside of any source root", file));
      return "unable.to.find.name";
    } else {
      return typeNames.get(0);
    }
  }

  @NotNull
  public static String getInjectedFileSuffix(@NotNull VirtualFileWindow window, @NotNull IModule gsModule) {
    final Project project = (Project) gsModule.getExecutionEnvironment().getProject().getNativeProject();

    final PsiFile host = PsiManager.getInstance(project).findFile(window.getDelegate());
    final int startOffset = window.getDocumentWindow().getHostRanges()[0].getStartOffset();
    final PsiElement hostElement = host.findElementAt(startOffset);

    final String unique;
    final XmlAttribute attr = PsiTreeUtil.getParentOfType(hostElement, XmlAttribute.class);
    if (attr != null) {
      final XmlLocation loc = getXmlAttributeLocation(attr);
      unique = loc.toString();
    } else {
      final XmlTag tag = PsiTreeUtil.getParentOfType(hostElement, XmlTag.class);
      if (tag != null) {
        final XmlLocation loc = getXmlTagLocation(tag);
        unique = loc.toString();
      } else {
        unique = Integer.toString(startOffset);
      }
    }
    return MAGIC_INJECTED_SUFFIX + unique;
  }

  @Nullable
  public static IModule getModule(@Nullable Module ijModule) {
    return ijModule == null ? null : TypeSystem.getExecutionEnvironment(PluginLoaderUtil.getFrom(ijModule.getProject())).getModule(ijModule.getName());
  }

  @NotNull
  public static IModule getModule(@NotNull IModule gsModule) {
    return (IModule) gsModule.getNativeModule();
  }

  public static IType getConcreteType(IType type) {
    while (type instanceof IMetaType) {
      type = ((IMetaType) type).getType();
    }

    while (type.isArray()) {
      type = type.getComponentType();
    }

    while (type.isParameterizedType()) {
      type = type.getGenericType();
    }

    if (type instanceof ITypeVariableType) {
      type = getConcreteType(((ITypeVariableType) type).getBoundingType());
    }

    return type;
  }

  public static IType getTrueEnclosingType(@NotNull IType type) {
    IType enclosingType = type.getEnclosingType();
    while (enclosingType instanceof IBlockClass) {
      enclosingType = enclosingType.getEnclosingType();
    }
    return enclosingType;
  }

  @NotNull
  public static IDEAResource toIResource(VirtualFile file) {
    if (file.isDirectory()) {
      return toIDirectory(file);
    } else {
      return toIFile(file);
    }
  }

  public static IDEAFile toIFile(VirtualFile file) {
    return ((IDEAFileSystem) CommonServices.getFileSystem()).getIFile(file);
  }

  @NotNull
  public static IDEADirectory toIDirectory(VirtualFile file) {
    return ((IDEAFileSystem) CommonServices.getFileSystem()).getIDirectory(file);
  }

  public static void showDiffWindow(final String text1, final String text2, final String title1, final String title2, final String title, final Project project) {
    Thread.dumpStack();

    IDEAUtil.runInDispatchThread(new Runnable() {
      public void run() {
        try {
          final DiffPanel diffPanel = DiffManager.getInstance().createDiffPanel(null, project,null);
          final SimpleDiffRequest diff = new SimpleDiffRequest(project, "Diff");
          diff.setContents(new SimpleContent(text1), new SimpleContent(text2));
          diffPanel.setDiffRequest(diff);
          diffPanel.setTitle1(title1);
          diffPanel.setTitle2(title2);

          final DialogBuilder builder = new DialogBuilder(project);
          builder.setCenterPanel(diffPanel.getComponent());
          builder.addDisposable(diffPanel);
          builder.setPreferedFocusComponent(diffPanel.getPreferredFocusedComponent());
          builder.removeAllActions();
          builder.setTitle(title);

          final AnAction action = new TypeSystemAwareAction() {
            public void actionPerformed(final AnActionEvent e) {
              builder.getDialogWrapper().close(0);
            }
          };
          action.registerCustomShortcutSet(new CustomShortcutSet(KeymapManager.getInstance().getActiveKeymap().getShortcuts("CloseContent")), diffPanel.getComponent());
          builder.showModal(true);
        } catch (Throwable e) {
          throw new RuntimeException("Cannot show diff panel");
        }
      }
    }, false);
  }

  // TODO: use GosuProperties class here somehow
  @NotNull
  public static List<String> getGosuPropertyNames(@NotNull final PsiMethod method) {
    List<String> propNames = new ArrayList<>();
    String name = method.getName();
    int nameLen = name.length();
    String returnTypeName = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
      @Override
      public String compute() {
        return method.getReturnType().getCanonicalText();
      }
    });
    if (nameLen > 3 && name.startsWith("get") &&
        !returnTypeName.equals("void") &&
        method.getParameterList().getParametersCount() == 0) {
      propNames.add(name.substring(3));
      propNames.add("is" + name.substring(3));
    } else if (nameLen > 3 && name.startsWith("set") &&
        returnTypeName.equals("void") &&
        method.getParameterList().getParametersCount() == 1) {
      propNames.add(name.substring(3));
    } else if (nameLen > 2 && name.startsWith("is") &&
        (returnTypeName.equals("boolean") ||
            returnTypeName.equals("java.lang.Boolean")) &&
        method.getParameterList().getParametersCount() == 0) {
      propNames.add(name.substring(2));
      propNames.add("get" + name.substring(2));
    } else if (method instanceof IGosuMethod && ((IGosuMethod) method).isForProperty()) {
      if (returnTypeName.equals("void")) {
        propNames.add("set" + name);
      } else {
        propNames.add("get" + name);
        propNames.add("is" + name);
      }
    }
    return propNames;
  }

  public static void closeAllGosuEditors(@NotNull Project project, @Nullable IModule gsModule) {
    final FileEditorManager manager = PsiAwareFileEditorManagerImpl.getInstance(project);
    for (VirtualFile file : manager.getOpenFiles()) {
      if (GosuFileTypes.isGosuFile(file)) {
        if (gsModule == null || GosuModuleUtil.findModuleForFile(file, project) == gsModule) {
          manager.closeFile(file);
        }
      }
    }
  }

  @NotNull
  public static JFrame getFrame(Project project) {
    return (JFrame) WindowManager.getInstance().getIdeFrame(project);
  }

  @NotNull
  public static JFrame getFrame() {
    return (JFrame) WindowManager.getInstance().getIdeFrame(null);
  }

  // ID generation
  @Nullable
  private static Iterator<XmlTag> selfWithParents(XmlTag tag) {
    return new AbstractLinkedIterator<XmlTag>(tag) {
      @Nullable
      protected XmlTag computeNext(@Nullable XmlTag previous) {
        return previous != null ? previous.getParentTag() : null;
      }
    };
  }

  @Nullable
  private static Iterator<XmlTagChild> selfWithPrevSiblings(XmlTagChild element) {
    return new AbstractLinkedIterator<XmlTagChild>(element) {
      @Nullable
      protected XmlTagChild computeNext(@Nullable XmlTagChild previous) {
        return previous != null ? previous.getPrevSiblingInTag() : null;
      }
    };
  }

  private static <T extends XmlTagChild> Iterator<T> selfWithPrevSiblingsOfType(XmlTagChild element, Class<T> klass) {
    return Iterators.filter(selfWithPrevSiblings(element), klass);
  }

  private static final Function<XmlTag, String> GET_XML_TAG_NAME = new Function<XmlTag, String>() {
    @NotNull
    @Override
    public String apply(@Nullable XmlTag tag) {
      return tag.getName();
    }
  };


  protected static List<XmlLocation.Segment> getPath(XmlTag tag) {
    final List<XmlLocation.Segment> path = Lists.newArrayList();
    for (XmlTag parent : skip(reverse(newArrayList(selfWithParents(tag))), 1)) {
      final String name = parent.getName();
      final int index = size(filter(
          selfWithPrevSiblingsOfType(parent, XmlTag.class),
          compose(equalTo(name), GET_XML_TAG_NAME))) - 1;
      path.add(new XmlLocation.Segment(name, index));
    }
    return ImmutableList.copyOf(path);
  }

  @NotNull
  public static XmlLocation getXmlTagLocation(@NotNull XmlTag tag) {
    final XmlFile file = (XmlFile) tag.getContainingFile();
    return new XmlLocation(file.getRootTag().getName(), getPath(tag));
  }

  @NotNull
  public static XmlLocation getXmlAttributeLocation(@NotNull XmlAttribute attr) {
    final XmlTag tag = attr.getParent();
    final XmlFile file = (XmlFile) tag.getContainingFile();
    return new XmlLocation(file.getRootTag().getName(), getPath(tag), attr.getName());
  }

  // Reporting errros
  public static void showError(@NotNull String title, @NotNull Throwable t) {
    System.err.println("DEV MODE ERROR REPORTING (REMOVE IN PRODUCTION)");
    t.printStackTrace();

    while (t.getCause() != null) {
      t = t.getCause();
    }
    String realError = t.getMessage();
    realError = realError != null ? realError : t.getClass().getSimpleName();
    realError = formatToHTML(realError);
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, realError, NotificationType.ERROR));
  }

  public static void showNonFatalError(String title, String text) {
    if (_inTestMode) {
      throw new RuntimeException(title + ": " + text);
    }

    final String htmlText = formatToHTML(text + "\n" + "<b><font size=\"3\" color=\"red\">This is a bug and you need to report it.</font></b>");
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, "Oops! " + title, htmlText, NotificationType.WARNING));
  }

  public static void showNonFatalError(String title, @NotNull Throwable e) {
    if (ExceptionUtil.isWrappedCanceled(e)) {
      if (e instanceof ProcessCanceledException) {
        throw (ProcessCanceledException) e;
      } else {
        throw new ProcessCanceledException(e);
      }
    }
    showNonFatalError(title, e.getMessage());
    ERROR_LOGGER.handle(new IdeaLoggingEvent(title, e));
  }

  public static void showInfo(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.INFORMATION));
  }

  public static void showWarning(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.WARNING));
  }

  public static void showError(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.ERROR));
  }

  public static void showBaloonlessError(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new BaloonlessNotification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.ERROR));
  }

  private static String formatToHTML(@NotNull String realError) {
    return realError.replaceAll("\\n", "<br>");
  }

  @Nullable
  public static PsiClass getContainingClass(@Nullable PsiElement element) {
    while (element != null && !(element instanceof PsiClass)) {
      element = element.getParent();
    }
    return (PsiClass) element;
  }

  @NotNull
  public static List<VirtualFile> getTypeResourceFiles(@NotNull IType type) {
    final List<VirtualFile> result = Lists.newArrayList();
    if (type instanceof IFileBasedType) {
      for (IFile file : ((IFileBasedType) type).getSourceFiles()) {
        result.add(((IDEAFile) file).getVirtualFile());
      }
    }
    return result;
  }

  @NotNull
  public static List<String> getTypesForFile(IModule module, VirtualFile file) {
    return Arrays.asList(TypeSystem.getTypesForFile(module, toIFile(file)));
  }

  @NotNull
  public static List<String> getTypesForFiles(IModule module, List<VirtualFile> files) {
    final List<String> types = Lists.newArrayList();
    for (VirtualFile file : files) {
      types.addAll(getTypesForFile(module, file));
    }
    return types;
  }

  public static IGosuProgram parseProgram(@NotNull IGosuParser parser, @NotNull ParserOptions options, ModuleFileContext context, String contents) throws ParseResultsException {
    final ParserOptions parserOptions = options
        .withParser(parser)
        .withTypeUsesMap(parser.getTypeUsesMap())
        .withFileContext(context);

    return GosuParserFactory.createProgramParser()
        .parseExpressionOrProgram(contents, parser.getSymbolTable(), parserOptions)
        .getProgram();
  }

  @NotNull
  public static String removeJarSeparator(@NotNull String path) {
    if (path.endsWith(JarFileSystem.JAR_SEPARATOR)) {
      path = path.substring(0, path.length() - JarFileSystem.JAR_SEPARATOR.length());
    }
    return path;
  }


  @Nullable
  public static PsiLanguageInjectionHost getInjectionHost(@NotNull PsiElement element) {
    PsiFile psiFile = element.getContainingFile();
    psiFile = psiFile.getOriginalFile();
    psiFile = InjectedElementEditor.getOriginalFile(psiFile.getOriginalFile());

    // Fast
    final PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(psiFile.getProject()).getInjectionHost(psiFile);
    if (host != null) {
      return host;
    }

    // Slow
    final VirtualFile file = psiFile.getVirtualFile();
    if (file instanceof VirtualFileWindow) {
      final VirtualFileWindow window = (VirtualFileWindow) file;
      final PsiFile psiFileHost = PsiManager.getInstance(psiFile.getProject()).findFile(window.getDelegate());
      final int startOffset = window.getDocumentWindow().getHostRanges()[0].getStartOffset();
      final PsiElement hostElement = psiFileHost.findElementAt(startOffset);
      return PsiTreeUtil.getParentOfType(hostElement, PsiLanguageInjectionHost.class);
    }

    return null;
  }

  public static IType getType(PsiClass psiClass) {
    final String qualifiedName = psiClass.getQualifiedName();
    final IModule module = GosuModuleUtil.findModuleForPsiElement(psiClass);
    TypeSystem.pushModule(module);
    try {
      return TypeSystem.getByFullNameIfValid(qualifiedName, module);
    } finally {
      TypeSystem.popModule(module);
    }
  }

  public static IType getType(PsiClass psiClass, IModule module) {
    TypeSystem.pushModule(module);
    try {
      return TypeSystem.getByFullNameIfValid(psiClass.getQualifiedName(), module);
    } catch (NoClassDefFoundError e) {
      // Probably, can't load dependencies of the class, just ignore the type itself
      // FIXME: better way of doing that?
      return null;
    }
    finally {
      TypeSystem.popModule(module);
    }
  }

  public static String collectMessage(Throwable e) {
    String message = "";
    do {
      message += "- " + e.getMessage() + "\n";
      e = e.getCause();
    } while (e != null);
    return message;
  }

  public static PsiElement resolveFeatureAtLocation( PsiElement context, LocationInfo location ) {
    if ( location == null ) {
      return null;
    }
    final IFile file = CommonServices.getFileSystem().getIFile(location.getFileUrl());
    Project project = context.getProject();
    VirtualFile vfile = ((IDEAResource) file ).getVirtualFile();
    if (vfile == null) {
      return null;
    }
    final PsiFile psiFile = PsiManagerImpl.getInstance( project ).findFile( vfile );
    final Document document = PsiDocumentManagerImpl.getInstance( project ).getDocument( psiFile );
    final int offset = document.getLineStartOffset( location.getLineNumber() - 1 ) + location.getColumnNumber() - 2;
    PsiElement element = psiFile.findElementAt( offset ); // empty element end
    element = element.getParent(); // element definition that contains the empty element end
    return element;
  }

  public static void settleModalEventQueue() {
    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    while (eventQueue.peekEvent() != null) {
      try {
        AWTEvent event = eventQueue.getNextEvent();
        Object src = event.getSource();
        if (event instanceof ActiveEvent) {
          ((ActiveEvent) event).dispatch();
        } else if (src instanceof Component) {
          ((Component) src).dispatchEvent(event);
        } else if (src instanceof MenuComponent) {
          ((MenuComponent) src).dispatchEvent(event);
        }
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static boolean underGosuSources(VirtualFile dir, Project project) {
    if (dir != null && dir.isDirectory()) {
      final IModule module = GosuModuleUtil.findModuleForFile(dir, project);
      if (module != null) {
        IResource sourceRoot = getSourceRoot(module, IDEAUtil.toIDirectory(dir));
        if (sourceRoot != null && sourceRoot.getPath().getPathString().endsWith("gsrc")) {
          return true;
        }
      }
    }
    return false;
  }

  private static IResource getSourceRoot(IModule module, IResource resource) {
    for (IDirectory src : module.getSourcePath()) {
      if (resource.isDescendantOf(src)) {
        return src;
      }
    }
    return null;
  }

  public static VirtualFile findSourceRoot(VirtualFile virtualFile, Project project) {
    Module module = ModuleUtil.findModuleForFile(virtualFile, project);
    if (module != null) {
      final String path = virtualFile.getPath();
      for (VirtualFile sourceRoot : ModuleRootManager.getInstance(module).getSourceRoots()) {
        String sourcePath = sourceRoot.getPath();
        if (path.startsWith(sourcePath + "/") || path.equals(sourcePath)) {
          return sourceRoot;
        }
      }
    }
    return null;
  }

  public static boolean isSourceRoot(VirtualFile file, Project project) {
    return file.equals(IDEAUtil.findSourceRoot(file, project));
  }
}
