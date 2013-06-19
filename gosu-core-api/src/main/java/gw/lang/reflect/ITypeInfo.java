/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.GosuShop;
import gw.lang.parser.ICoercer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ITypeInfo extends IAnnotatedFeatureInfo
{
  public static final String TYPEINFO_EXT = "TypeInfo";

  /**
   * @return An <b>unmodifiable random access</b> list of <code>IPropertyInfo</code>
   *         instances. The list is sorted ascending by name. Returns an empty list if
   *         there are no properties.
   */
  public List<? extends IPropertyInfo> getProperties();

  /**
   * Get a property mapped to the specified name.
   *
   * @param propName The property name.
   *
   * @return An IPropertyInfo corresponding to the property name.
   */
  public IPropertyInfo getProperty( CharSequence propName );

  /**
   * @return An <b>unmodifiable random access</b> list of <code>IMethodInfo</code>
   *         instances. The list is sorted ascending by name. Returns an empty list if
   *         there are no methods.
   */
  public MethodList getMethods();

  /**
   * Returns a IMethodInfo matching the specified name and parameter types or
   * null if no match is found.
   * <p/>
   * Note <code>params</code> must <i>exactly</i> match those of the target
   * method in number, order, and type. If null, <code>params</code> is treated
   * as an empty array.
   *
   * @param methodName The name of the method to find.
   * @param params     Represents the <i>exact</i> number, order, and type of parameters
   *                   in the method. A null value here is treated as an empty array.
   *
   * @return A IMethodInfo matching the name and parameter types.
   */
  public IMethodInfo getMethod( CharSequence methodName, IType... params );

  /**
   * Returns a IMethodInfo matching the specified name and has parameter types that
   * produce the best match.
   * <p/>
   * If there is a tie with method names then this will throw an illegal argument exception.
   *
   * @param method The name of the method to find.
   * @param params Represents the <i>exact</i> number, order, and type of parameters
   *               in the method. A null value here is treated as an empty array.
   *
   * @return A IMethodInfo matching the name and parameter types.
   */
  public IMethodInfo getCallableMethod( CharSequence method, IType... params );

  /**
   * @return An <b>unmodifiable random access</b> list of <code>IConstructorInfo</code>
   *         instances. The list is sorted ascending by name. Returns an empty list if
   *         there are no constructors.
   */
  public List<? extends IConstructorInfo> getConstructors();

  /**
   * Returns a IConstructorInfo that has parameter types that produce the best match.
   * <p/>
   * If there is a tie with method names then this will throw an illegal argument exception.
   *
   * @param params Represents the <i>exact</i> number, order, and type of parameters
   *               in the constructor. A null value here is treated as an empty array.
   *
   * @return A IConstructorInfo matching the parameter types.
   */
  public IConstructorInfo getConstructor( IType... params );

  /**
   * Returns a IConstructorInfo matching the specified parameter types or null
   * if no match is found.
   *
   * @param params Represents the <i>exact</i> number, order, and type of parameters
   *               in the constructor. A null value here is treated as an empty array.
   *
   * @return A IConstructorInfo matching the parameter types.
   */
  public IConstructorInfo getCallableConstructor( IType... params );

  /**
   * @return An <b>unmodifiable random access</b> list of <code>IEventInfo</code>
   *         instances. The list is sorted ascending by name. Returns an empty list if
   *         ther are no events.
   */
  public List<? extends IEventInfo> getEvents();

  /**
   * Get an event mapped to the specified name.
   *
   * @param event The event name.
   *
   * @return An IEventInfo corresponding to the event name.
   */
  public IEventInfo getEvent( CharSequence event );

  /**
   * A general purpose class for finding methods and constructors.
   */
  public static class FIND
  {
    private static final IType[] EMPTY_TYPES = IType.EMPTY_ARRAY;

    public static IMethodInfo method( MethodList methods, CharSequence method, IType... params )
    {
      params = params == null ? EMPTY_TYPES : params;
      for( int i = 0; i < methods.size; i++ )
      {
        IMethodInfo methodInfo = (IMethodInfo) methods.data[i];
        if( methodInfo.getDisplayName().equals( method.toString() ) ) {
          IParameterInfo[] paramInfos = methodInfo.getParameters();
          if (areParamsEqual( paramInfos, params )) {
            return methodInfo;
          }
        }
      }
      return null;
    }

    public static IConstructorInfo constructor( List<? extends IConstructorInfo> constructors, IType... params )
    {
      params = params == null ? EMPTY_TYPES : params;
      for( IConstructorInfo constructorInfo : constructors )
      {
        IParameterInfo[] paramInfos = constructorInfo.getParameters();
        if( areParamsEqual( paramInfos, params ) )
        {
          return constructorInfo;
        }
      }
      return null;
    }

    /**
     * If there is a tie this method will throw an IllegalArgumentException.  This method is not strict, which means that
     * clients calling this method may get back a method where the arguments must be coerced to the expected parameter tyeps.
     * If you wish strict behavior call {@link #callableMethodStrict(MethodList, CharSequence, IType[])}
     */
    public static IMethodInfo callableMethod( MethodList methods, CharSequence method, IType... params )
    {
      return callableMethodImpl( methods, method, false, params );
    }

    /**
     * If there is a tie this method will throw an IllegalArgumentException.  This version is strict, which means that
     * clients calling this method do not need to do any coercion of arguments in order to invoke the IMethodInfo.
     */
    public static IMethodInfo callableMethodStrict( MethodList methods, CharSequence method, IType... params )
    {
      return callableMethodImpl( methods, method, true, params );
    }

    /**
     * If there is a tie this method will throw an IllegalArgumentException.
     */
    private static IMethodInfo callableMethodImpl( MethodList methods, CharSequence method, boolean strict, IType... params )
    {
      Map<IFunctionType, IMethodInfo> mis = new HashMap<IFunctionType, IMethodInfo>();
      params = params == null ? EMPTY_TYPES : params;
      for( int i = 0; i < methods.size; i++ )
      {
        IMethodInfo methodInfo = (IMethodInfo) methods.data[i];
        if( methodInfo.getDisplayName().equals( method.toString() ) &&
            methodInfo.getParameters().length == params.length )
        {
          mis.put( new FunctionType( methodInfo ), methodInfo );
        }
      }
      List<MethodScore> list = scoreMethods( new ArrayList<IInvocableType>( mis.keySet() ), Arrays.asList( params ), Collections.EMPTY_LIST );
      if( list.size() == 0 )
      {
        return null;
      }
      else if( list.size() > 1 && list.get( 0 ).getScore() == list.get( 1 ).getScore() )
      {
        throw new IllegalArgumentException( "Ambiguous methods: There is more than one method named " + method + " that accepts args " + Arrays.asList( params ) );
      }
      IInvocableType rawFunctionType = list.get( 0 ).getRawFunctionType();
      if( strict && !areParamsCompatible( rawFunctionType.getParameterTypes(), params ) )
      {
        return null;
      }
      return mis.get( rawFunctionType );
    }

    private static boolean areParamsCompatible( IType[] actualParamTypes, IType[] userParamTypes )
    {
      for( int i = 0; i < actualParamTypes.length; i++ )
      {
        IType actualParamType = actualParamTypes[i];
        IType userParamType = userParamTypes[i];
        if( !actualParamType.isAssignableFrom( userParamType ) )
        {
          return false;
        }
      }
      return true;
    }

    /**
     * If there is a tie this method will throw an IllegalArgumentException.  This method is not strict, which means that
     * clients calling this method may get back a constructor where the arguments must be coerced to the expected parameter tyeps.
     * If you wish strict behavior call {@link #callableConstructorStrict(java.util.List, IType[])}
     */

    public static IConstructorInfo callableConstructor( List<? extends IConstructorInfo> constructors, IType... params )
    {
      return callableConstructorImpl( constructors, false, params );
    }


    /**
     * If there is a tie this method will throw an IllegalArgumentException.  This version is strict, which means that
     * clients calling this method do not need to do any coercion of arguments in order to invoke the IConstructorInfo.
     */
    public static IConstructorInfo callableConstructorStrict( List<? extends IConstructorInfo> constructors, IType... params )
    {
      return callableConstructorImpl( constructors, true, params );
    }

    private static IConstructorInfo callableConstructorImpl( List<? extends IConstructorInfo> constructors, boolean strict, IType... params )
    {
      Map<IConstructorType, IConstructorInfo> cis = new HashMap<IConstructorType, IConstructorInfo>();
      params = params == null ? EMPTY_TYPES : params;
      for( IConstructorInfo constructorInfo : constructors )
      {
        if( params.length == constructorInfo.getParameters().length )
        {
          cis.put( GosuShop.getConstructorInfoFactory().makeConstructorType( constructorInfo ), constructorInfo );
        }
      }
      List<MethodScore> list = scoreMethods( new ArrayList<IInvocableType>( cis.keySet() ), Arrays.asList( params ), Collections.EMPTY_LIST );
      if( list.size() == 0 )
      {
        return null;
      }
      else if( list.size() > 1 && list.get( 0 ).getScore() == list.get( 1 ).getScore() )
      {
        throw new IllegalArgumentException( "Ambiguous constructors: There is more than one constructor that accepts args " + Arrays.asList( params ) );
      }
      IInvocableType rawFunctionType = list.get( 0 ).getRawFunctionType();
      if( strict && !areParamsCompatible( rawFunctionType.getParameterTypes(), params ) )
      {
        return null;
      }
      return cis.get( rawFunctionType );
    }

    public static boolean areParamsEqual( IParameterInfo srcArgs[],
                                          IType testArgs[] )
    {
      if( srcArgs.length == testArgs.length )
      {
        for( int j = 0; j < srcArgs.length; j++ )
        {
          IType methodParamType = srcArgs[j].getFeatureType();
          IType testParamType = testArgs[j];

          // If one of the types is a paramerized type and the other is the generic version, down cast the param type.

          while( methodParamType.isArray() && testParamType.isArray() )
          {
            methodParamType = methodParamType.getComponentType();
            testParamType = testParamType.getComponentType();
          }

          if( methodParamType.isParameterizedType() && !testParamType.isParameterizedType() )
          {
            methodParamType = TypeSystem.getPureGenericType( methodParamType );
          }
          else if( testParamType.isParameterizedType() && !methodParamType.isParameterizedType() )
          {
            testParamType = TypeSystem.getPureGenericType( testParamType );
          }
          else if( typeVarsAreFromDifferentMethods( methodParamType, testParamType ) )
          {
            methodParamType = getConcreteBoundingType( methodParamType );
            testParamType = getConcreteBoundingType( testParamType );
          }

          if( methodParamType.isParameterizedType() )
          {
            if( !TypeSystem.getPureGenericType( methodParamType ).equals( TypeSystem.getPureGenericType( testParamType ) ) )
            {
              return false;
            }

            IType[] methodTypeParameters = methodParamType.getTypeParameters();
            IType[] testTypeParameters = testParamType.getTypeParameters();
            for( int i = 0; i < methodTypeParameters.length; i++ )
            {
              if( !getConcreteBoundingType( methodTypeParameters[i] ).isAssignableFrom( testTypeParameters[i] ) )
              {
                return false;
              }
            }
          }
          else if( !methodParamType.equals( testParamType ) )
          {
            return false;
          }
        }
        return true;
      }

      return false;
    }

    private static boolean typeVarsAreFromDifferentMethods( IType methodParamType, IType testParamType )
    {
      if( methodParamType instanceof ITypeVariableArrayType && testParamType instanceof ITypeVariableArrayType )
      {
        return typeVarsAreFromDifferentMethods( methodParamType.getComponentType(), testParamType.getComponentType() );
      }
      else
      {
        return testParamType instanceof ITypeVariableType &&
               testParamType.getEnclosingType() instanceof FunctionType &&
               methodParamType instanceof ITypeVariableType &&
               methodParamType.getEnclosingType() instanceof FunctionType &&
               testParamType.getEnclosingType() != methodParamType.getEnclosingType();
      }
    }

    private static IType getConcreteBoundingType( IType type )
    {
      if( type instanceof ITypeVariableType )
      {
        return getConcreteBoundingType( ((ITypeVariableType)type).getBoundingType() );
      }
      else if( type instanceof ITypeVariableArrayType )
      {
        return getConcreteBoundingType( type.getComponentType() );
      }
      return type;
    }

    //## todo: Rewrite this piece of total shit.  Maybe use this technique: http://stackoverflow.com/questions/14315437/get-best-matching-overload-from-set-of-overloads
    //## todo: Note it'll be easier to manage if the best match is designed to have the _lowest_ score.  Again the link above provides a decent strategy.
    public static List<MethodScore> scoreMethods( List<? extends IInvocableType> listFunctionTypes, List<IType> argTypes, List<IType> inferringTypes )
    {
      ArrayList<MethodScore> scores = new ArrayList<MethodScore>();
      // if there is only one method, don't bother scoring
      if( listFunctionTypes.size() == 1 )
      {
        MethodScore score = new MethodScore();
        IInvocableType functionType = listFunctionTypes.get( 0 );
        score.setRawFunctionType( functionType );
        score.setValid( true );
        scores.add( score );
      }
      else
      {
        // if there are multiple methods, create scores for them
        for( IInvocableType functionType : listFunctionTypes )
        {
          MethodScore score = new MethodScore();
          score.setValid( true );
          score.setRawFunctionType( functionType );
          scores.add( score );
        }

        int assignabilityAmt = (argTypes.size() * (ICoercer.MAX_PRIORITY + 1)) + 1; // the amount to increment assignability matches by

        for( int i = 0; i < argTypes.size(); i++ )
        {
          IType exprType = argTypes.get( i );

          // keep track of all methods that are assignable at this parameter position
          Map<IType, List<MethodScore>> bestMatches = new HashMap<IType, List<MethodScore>>();

          for( MethodScore score : scores )
          {
            IType[] parameterTypes = score.getRawFunctionType().getParameterTypes();
            if( argTypes.size() == parameterTypes.length )
            {

              IType parameterType = TypeSystem.boundTypes( parameterTypes[i], inferringTypes );
              if( parameterType.isAssignableFrom( exprType ) )
              {
                score.incScore( assignabilityAmt );

                // remove any existing assignable methods that are supertypes of this
                for( Iterator<Map.Entry<IType, List<MethodScore>>> it = bestMatches.entrySet().iterator(); it.hasNext(); )
                {
                  Map.Entry<IType, List<MethodScore>> entry = it.next();
                  if( !parameterType.equals( entry.getKey() ) )
                  {
                    if( parameterType.isAssignableFrom( entry.getKey() ) )
                    {
                      // an existing method score is better than this one, so null it out and break
                      score = null;
                      break;
                    }
                    else if( entry.getKey().isAssignableFrom( parameterType ) )
                    {
                      it.remove();
                    }
                  }
                }
                if( score != null )
                {
                  List<MethodScore> bestMatchList = bestMatches.get( parameterType );
                  if( bestMatchList == null )
                  {
                    bestMatchList = new ArrayList<MethodScore>();
                    bestMatches.put( parameterType, bestMatchList );
                  }
                  bestMatchList.add( score );
                }
              }
              else
              {
                ICoercer iCoercer = CommonServices.getCoercionManager().findCoercer( parameterType, exprType, false );
                if( iCoercer != null )
                {
                  score.incScore( iCoercer.getPriority( parameterType, exprType ) + 1 );
                }
                else
                {
                  // if the argument is neither coercable nor assignable, reset the method score to a very low value
                  score.setScore( Integer.MIN_VALUE );
                }
              }
            }
            else
            {
              score.setScore( Long.MIN_VALUE );
            }
          }

          // if there were any best matches, increment their scores
          if( !bestMatches.isEmpty() )
          {
            for( List<MethodScore> methodScores : bestMatches.values() )
            {
              for( MethodScore score : methodScores )
              {
                score.incScore( assignabilityAmt );
              }
            }
          }
        }

        Collections.sort( scores );
      }
      return scores;
    }

  }
}
