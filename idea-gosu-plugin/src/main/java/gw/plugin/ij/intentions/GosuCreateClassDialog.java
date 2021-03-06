/*
 * Copyright 2013 Guidewire Software, Inc.
 */
package gw.plugin.ij.intentions;

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassKind;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDirectory;
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.RecentsManager;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class GosuCreateClassDialog extends DialogWrapper {
  private final JLabel myInformationLabel = new JLabel("#");
  private final JLabel myPackageLabel = new JLabel(CodeInsightBundle.message("dialog.create.class.destination.package.label"));
  @NotNull
  private final ReferenceEditorComboWithBrowseButton myPackageComponent;
  private final JTextField myTfClassName = new MyTextField();
  @NotNull
  private final Project myProject;
  @Nullable
  private PsiDirectory myTargetDirectory;
  private final String myClassName;
  private final boolean myClassNameEditable;
  private final Module myModule;
  @NonNls private static final String RECENTS_KEY = "CreateClassDialog.RecentsKey";

  public GosuCreateClassDialog(@NotNull Project project,
                               @NotNull String title,
                               String targetClassName,
                               @Nullable String targetPackageName,
                               @NotNull GosuCreateClassKind kind,
                               boolean classNameEditable,
                               Module defaultModule) {
    super(project, true);
    myClassNameEditable = classNameEditable;
    myModule = defaultModule;
    myClassName = targetClassName;
    myProject = project;
    myPackageComponent = new PackageNameReferenceEditorCombo( targetPackageName != null ? targetPackageName : "", myProject, RECENTS_KEY, CodeInsightBundle.message("dialog.create.class.package.chooser.title"));
    myPackageComponent.setTextFieldPreferredWidth(40);

    init();

    if (!myClassNameEditable) {
      setTitle(CodeInsightBundle.message("dialog.create.class.name", StringUtil.capitalize(kind.getDescription()), targetClassName));
    }
    else {
      myInformationLabel.setText(CodeInsightBundle.message("dialog.create.class.label", kind.getDescription()));
      setTitle(title);
    }

    myTfClassName.setText(myClassName);
  }

  @NotNull
  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction()};
  }

  public JComponent getPreferredFocusedComponent() {
    return myClassNameEditable ? myTfClassName : myPackageComponent.getChildComponent();
  }

  protected JComponent createCenterPanel() {
    return new JPanel(new BorderLayout());
  }

  protected JComponent createNorthPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbConstraints = new GridBagConstraints();

    gbConstraints.insets = new Insets(4, 8, 4, 8);
    gbConstraints.fill = GridBagConstraints.HORIZONTAL;
    gbConstraints.anchor = GridBagConstraints.WEST;

    if (myClassNameEditable) {
      gbConstraints.weightx = 0;
      gbConstraints.gridwidth = 1;
      panel.add(myInformationLabel, gbConstraints);
      panel.setBorder(IdeBorderFactory.createRoundedBorder());
      gbConstraints.insets = new Insets(4, 8, 4, 8);
      gbConstraints.gridx = 1;
      gbConstraints.weightx = 1;
      gbConstraints.gridwidth = 1;
      gbConstraints.fill = GridBagConstraints.HORIZONTAL;
      gbConstraints.anchor = GridBagConstraints.WEST;
      panel.add(myTfClassName, gbConstraints);

      myTfClassName.getDocument().addDocumentListener(new DocumentAdapter() {
        protected void textChanged(DocumentEvent e) {
          getOKAction().setEnabled(JavaPsiFacade.getInstance(myProject).getNameHelper().isIdentifier(myTfClassName.getText()));
        }
      });
      getOKAction().setEnabled(StringUtil.isNotEmpty(myClassName));
    }

    gbConstraints.gridx = 0;
    gbConstraints.gridy = 2;
    gbConstraints.weightx = 0;
    gbConstraints.gridwidth = 1;
    panel.add(myPackageLabel, gbConstraints);

    gbConstraints.gridx = 1;
    gbConstraints.weightx = 1;

    new TypeSystemAwareAction() {
      public void actionPerformed(AnActionEvent e) {
        myPackageComponent.getButton().doClick();
      }
    }.registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK)), myPackageComponent.getChildComponent());

    JPanel _panel = new JPanel(new BorderLayout());
    _panel.add(myPackageComponent, BorderLayout.CENTER);
    panel.add(_panel, gbConstraints);

    return panel;
  }

  @Nullable
  public PsiDirectory getTargetDirectory() {
    return myTargetDirectory;
  }

  @NotNull
  private String getPackageName() {
    String name = myPackageComponent.getText();
    return name != null ? name.trim() : "";
  }

  private static class MyTextField extends JTextField {
    public Dimension getPreferredSize() {
      Dimension size = super.getPreferredSize();
      FontMetrics fontMetrics = getFontMetrics(getFont());
      size.width = fontMetrics.charWidth('a') * 40;
      return size;
    }
  }

  protected void doOKAction() {
    RecentsManager.getInstance(myProject).registerRecentEntry(RECENTS_KEY, myPackageComponent.getText());
    final String packageName = getPackageName();

    final String[] errorString = new String[1];
    CommandProcessor.getInstance().executeCommand(myProject, new Runnable() {
      public void run() {
        try {
          myTargetDirectory = myModule == null? PackageUtil.findOrCreateDirectoryForPackage(myProject, packageName, getBaseDir(packageName), true)
            : PackageUtil.findOrCreateDirectoryForPackage(myModule, packageName, getBaseDir(packageName), true, true);
          if (myTargetDirectory == null) {
            errorString[0] = ""; // message already reported by PackageUtil
            return;
          }
          errorString[0] = RefactoringMessageUtil.checkCanCreateClass(myTargetDirectory, getClassName());
        }
        catch (IncorrectOperationException e) {
          errorString[0] = e.getMessage();
        }
      }
    }, CodeInsightBundle.message("create.directory.command"), null);

    if (errorString[0] != null) {
      if (errorString[0].length() > 0) {
        Messages.showMessageDialog(myProject, errorString[0], CommonBundle.getErrorTitle(), Messages.getErrorIcon());
      }
      return;
    }
    super.doOKAction();
  }

  @Nullable
  protected PsiDirectory getBaseDir(String packageName) {
    return myModule == null? null : PackageUtil.findPossiblePackageDirectoryInModule(myModule, packageName);
  }

  public String getClassName() {
    if (myClassNameEditable) {
      return myTfClassName.getText();
    }
    else {
      return myClassName;
    }
  }
}
