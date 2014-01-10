package gw.plugin.ij.util;

import com.intellij.openapi.application.ApplicationManager;
import junit.framework.TestCase;

import static gw.plugin.ij.util.ExecutionUtil.*;

public class ExecutionUtilTest extends TestCase {

//  public void testSafeRunnable() {
//    final MockApp application = setupApplication();
//    final String[] threadName = new String[1];
//    final boolean[] inInWriteAction = new boolean[1];
//    ExecutionUtil.execute(WRITE | DISPATCH | BLOCKING, new SafeRunnable() {
//      public void execute() throws Exception {
//        threadName[0] = Thread.currentThread().getName();
//        inInWriteAction[0] = application.inWriteAction;
//      }
//    });
//    assertEquals(threadName[0], "AWT-EventQueue-0");
//    assertEquals(true, inInWriteAction[0]);
//  }
//
//  public void testSafeCallable() {
//    final MockApp application = setupApplication();
//    final String[] threadName = new String[1];
//    final boolean[] inInWriteAction = new boolean[1];
//    ExecutionUtil.execute(WRITE | DISPATCH | BLOCKING, new SafeCallable<Object>() {
//      public Object execute() throws Exception {
//        threadName[0] = Thread.currentThread().getName();
//        inInWriteAction[0] = application.inWriteAction;
//        return null;
//      }
//    });
//    assertEquals(threadName[0], "AWT-EventQueue-0");
//    assertEquals(true, inInWriteAction[0]);
//  }
//
//  private MockApp setupApplication() {
//    final MockApp application = new MockApp();
//    ApplicationManager.setApplication(application, new com.intellij.openapi.Disposable() {
//      public void dispose() {
//      }
//    });
//    return application;
//  }

  public void testFixMe() {
    // This test  doesn't dispose a new application properly. The tests that will run after this on TH will fail.
    assertTrue(false);
  }

}
