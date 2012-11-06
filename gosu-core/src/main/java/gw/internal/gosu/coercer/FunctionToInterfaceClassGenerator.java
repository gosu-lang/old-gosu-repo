/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.coercer;

import gw.lang.ir.builder.IRClassBuilder;
import gw.lang.ir.builder.IRMethodBuilder;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.internal.gosu.ir.builders.SimpleCompiler;
import gw.lang.ir.IRClass;
import gw.lang.ir.IRTypeConstants;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.function.IBlock;
import gw.lang.parser.ICoercer;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static gw.lang.ir.builder.IRBuilderMethods.*;

public class FunctionToInterfaceClassGenerator {

  private static Map<Class, Class> _classesToBlockCoercers = new HashMap<Class, Class>();

  public static synchronized void clearCachedClasses() {
    _classesToBlockCoercers.clear();
  }

  public static synchronized Class getBlockToInterfaceConversionClass( IJavaType typeToCoerceTo ) {
    Class coercionClass = _classesToBlockCoercers.get( typeToCoerceTo.getBackingClass() );
    if (coercionClass == null) {
      coercionClass = generateBlockToInterfaceConversionClass( typeToCoerceTo );
      _classesToBlockCoercers.put( typeToCoerceTo.getBackingClass(), coercionClass );
    }
    return coercionClass;
  }
  
  private static Class generateBlockToInterfaceConversionClass( IJavaType typeToCoerceTo ) {
    typeToCoerceTo = TypeLord.getPureGenericType( typeToCoerceTo );

    IRClassBuilder classBuilder = initializeClass( typeToCoerceTo );

    addFields( classBuilder );

    addConstructor( classBuilder );

    addInterfaceMethod( classBuilder, typeToCoerceTo );

    IRClass irClass = classBuilder.build();

    byte[] bytes = SimpleCompiler.INSTANCE.compile(irClass, false);
    return TypeSystem.getGosuClassLoader().defineClass( irClass.getName(), bytes );
  }

  private static IRClassBuilder initializeClass( IJavaType typeToCoerceTo ) {
    IRClassBuilder classBuilder = new IRClassBuilder( "__proxy.generated.blocktointerface.ProxyFor" + typeToCoerceTo.getRelativeName(), Object.class );
    classBuilder.withInterface( typeToCoerceTo );
    return classBuilder;
  }

  private static void addFields( IRClassBuilder classBuilder ) {
    classBuilder.createField().withName( "_block" ).withType( IBlock.class )._private().build();
    classBuilder.createField().withName( "_coercer" ).withType( ICoercer.class )._private().build();
    classBuilder.createField().withName( "_returnType" ).withType( IType.class )._private().build();
  }

  private static void addConstructor( IRClassBuilder classBuilder ) {
    IRMethodBuilder method = classBuilder.createConstructor();
    method._public().parameters("block", IBlock.class, "coercer", ICoercer.class, "returnType", IType.class).body(
            set("_block", var("block")),
            set("_coercer", var("coercer")),
            set("_returnType", var("returnType")),
            _superInit(),
            _return()
    );
  }

  private static void addInterfaceMethod( IRClassBuilder classBuilder, IJavaType typeToCoerceTo ) {

    final IJavaClassMethod proxiedMethod = getSingleMethod( typeToCoerceTo ).getMethod();

    IRMethodBuilder method = classBuilder.createMethod();
    method.name( proxiedMethod.getName() )
            ._public()
            .copyParameters( proxiedMethod )
            .returns( proxiedMethod.getReturnClassInfo() );

    List<IRExpressionBuilder> arrayContents = new ArrayList<IRExpressionBuilder>();
    for (int i = 0; i < proxiedMethod.getParameterTypes().length; i++) {
      arrayContents.add(var("arg" + i));
    }

    if (proxiedMethod.getReturnType() == JavaTypes.pVOID()) {
      method.body(
        field("_block").call("invokeWithArgs", newArray(Object.class, arrayContents)),
        _return()
      );
    } else {
      method.body(
        assign("value", IRTypeConstants.OBJECT, field("_block").call("invokeWithArgs", newArray(Object.class, arrayContents))),
        _if(field("_coercer").isNotNull()).then(
          assign("value", field("_coercer").call("coerceValue", field("_returnType"), var("value")))
        ),
        _return(var("value"))
      );
    }
  }

  private static IJavaMethodInfo getSingleMethod( IType interfaceType )
  {
    if( interfaceType.isInterface() && interfaceType instanceof IJavaType )
    {
      IJavaType javaIntrinsicType = (IJavaType)interfaceType;
      List<IMethodInfo> list = new ArrayList<IMethodInfo>( javaIntrinsicType.getTypeInfo().getMethods() );

      //extract all object methods since they are guaranteed to be implemented
      for( Iterator<? extends IMethodInfo> it = list.iterator(); it.hasNext(); )
      {
        IMethodInfo methodInfo = it.next();
        IParameterInfo[] parameterInfos = methodInfo.getParameters();
        IType[] paramTypes = new IType[parameterInfos.length];
        for( int i = 0; i < parameterInfos.length; i++ )
        {
          paramTypes[i] = parameterInfos[i].getFeatureType();
        }
        if( JavaTypes.OBJECT().getTypeInfo().getMethod( methodInfo.getDisplayName(), paramTypes ) != null ||
            methodInfo.getOwnersType() instanceof IGosuEnhancement)
        {
          it.remove();
        }
      }

      if( list.size() == 1 && list.get( 0 ) instanceof IJavaMethodInfo )
      {
        return (IJavaMethodInfo)list.get( 0 );
      }
    }
    return null;
  }

}
