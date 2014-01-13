/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.parser.IResolvingCoercer;
import gw.lang.parser.ICoercer;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.java.JavaTypes;
import gw.config.CommonServices;
import gw.util.concurrent.LockingLazyVar;

public class BasePrimitiveCoercer extends StandardCoercer implements IResolvingCoercer
{
  public static final LockingLazyVar<BasePrimitiveCoercer> DoublePCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(DoubleCoercer.instance(), JavaTypes.pDOUBLE(), JavaTypes.DOUBLE());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> FloatPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(FloatCoercer.instance(), JavaTypes.pFLOAT(), JavaTypes.FLOAT());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> BooleanPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(BooleanCoercer.instance(), JavaTypes.pBOOLEAN(), JavaTypes.BOOLEAN());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> BytePCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(ByteCoercer.instance(), JavaTypes.pBYTE(), JavaTypes.BYTE());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> ShortPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(ShortCoercer.instance(), JavaTypes.pSHORT(), JavaTypes.SHORT());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> CharPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(CharCoercer.instance(), JavaTypes.pCHAR(), JavaTypes.CHARACTER());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> IntPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(IntCoercer.instance(), JavaTypes.pINT(), JavaTypes.INTEGER());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> LongPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(LongCoercer.instance(), JavaTypes.pLONG(), JavaTypes.LONG());
    }
  };

  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        DoublePCoercer.clear();
        FloatPCoercer.clear();
        BooleanPCoercer.clear();
        BytePCoercer.clear();
        ShortPCoercer.clear();
        CharPCoercer.clear();
        IntPCoercer.clear();
        LongPCoercer.clear();
      }
    });
  }

  //The non-primitive coercer
  private final ICoercer _nonPrimitiveCoercer;
  private final IType _primitiveType;
  private final IType _nonPrimitveType;

  public BasePrimitiveCoercer( ICoercer nonPrimitiveCoercer, IType primitiveType, IType nonPrimitiveType )
  {
    _nonPrimitiveCoercer = nonPrimitiveCoercer;
    _primitiveType = primitiveType;
    _nonPrimitveType = nonPrimitiveType;
  }

  public final Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value == null )
    {
      return CommonServices.getCoercionManager().convertNullAsPrimitive( _primitiveType, false );
    }
    else
    {
      return _nonPrimitiveCoercer.coerceValue(typeToCoerceTo, value);
    }
  }

  @Override
  public boolean handlesNull()
  {
    return true;
  }

  public IType resolveType( IType target, IType source )
  {
    return target.isPrimitive() ? _primitiveType : _nonPrimitveType;
  }

  @Override
  public int getPriority( IType to, IType from )
  {
    int iPriority = 2;
    if(  to == from )
    {
      iPriority+=4;
    }
    else  if( isAWideingConversion( to, from ) )
    {
      iPriority+=3;
    }
    else if( hasLossOfPrecision(to, from) )
    {
      iPriority+=2;
    }
    else if( JavaTypes.OBJECT().equals( to ) )
    {
      iPriority++;
    }

    if( (isFloatFamily( to ) && isFloatFamily( from )) ||
            (isIntFamily( to ) && isIntFamily( from )) )
    {
      iPriority++;
    }
    return iPriority;
  }

  private boolean hasLossOfPrecision(IType to, IType from) {
    boolean[][] tab =
                      {                                        //TO
                          //FROM       boolean char    byte    short   int     long    float   double
                          /*boolean*/  {false,  false,  false,  false,  false,  false,  false,  false },
                          /*char   */  {false,  false,  false,  false,  false,  false,  false,  false },
                          /*byte   */  {false,  false,  false,  false,  false,  false,  false,  false },
                          /*short  */  {false,  false,  false,  false,  false,  false,  false,  false },
                          /*int    */  {false,  false,  false,  false,  false,  false,  true,   false },
                          /*long   */  {false,  false,  false,  false,  false,  false,  true,   true  },
                          /*float  */  {false,  false,  false,  false,  false,  false,  false,  false },
                          /*double */  {false,  false,  false,  false,  false,  false,  false,  false }
                      };

    final int i = getIndex(from);
    final int j = getIndex(to);
    if(i == -1 || j == -1 )
    {
      return false;
    }
    return tab[i][j];
  }

  private int getIndex(IType type) {
    if( type == JavaTypes.pBOOLEAN() || type == JavaTypes.BOOLEAN() )
    {
      return 0;
    }
    else if( type == JavaTypes.pCHAR() || type == JavaTypes.CHARACTER() )
    {
      return 1;
    }
    else if( type == JavaTypes.pBYTE() || type == JavaTypes.BYTE() )
    {
      return 2;
    }
    else if( type == JavaTypes.pSHORT() || type == JavaTypes.SHORT() )
    {
      return 3;
    }
    else if( type == JavaTypes.pINT() || type == JavaTypes.INTEGER() )
    {
      return 4;
    }
    else if( type == JavaTypes.pLONG() || type == JavaTypes.LONG() )
    {
      return 5;
    }
    else if( type == JavaTypes.pFLOAT() || type == JavaTypes.FLOAT() )
    {
      return 6;
    }
    else if( type == JavaTypes.pDOUBLE() || type == JavaTypes.DOUBLE() )
    {
      return 7;
    }
    return -1;
  }

  private boolean isAWideingConversion(IType to, IType from) {
    boolean[][] tab =
                        {                                   //TO
                          //FROM       boolean  char    byte    short   int     long    float   double
                          /*boolean*/  {true,   false,  false,  false,  false,  false,  false,  false},
                          /*char   */  {false,  true,   false,  false,  true,   true,   true,   true },
                          /*byte   */  {false,  false,  true,   true,   true,   true,   true,   true },
                          /*short  */  {false,  false,  false,  true,   true,   true,   true,   true },
                          /*int    */  {false,  false,  false,  false,  true,   true,   false,  true },
                          /*long   */  {false,  false,  false,  false,  false,  true,   false,  false},
                          /*float  */  {false,  false,  false,  false,  false,  false,  true,   true },
                          /*double */  {false,  false,  false,  false,  false,  false,  false,  true }
                        };

    final int i = getIndex(from);
    final int j = getIndex(to);
    if(i == -1 || j == -1 )
    {
      return false;
    }
    return tab[i][j];
  }

  private boolean isFloatFamily( IType type )
  {
    return type == JavaTypes.FLOAT() ||
           type == JavaTypes.pFLOAT() ||
           type == JavaTypes.DOUBLE() ||
           type == JavaTypes.pDOUBLE();
  }

  private boolean isIntFamily( IType type )
  {
    return type == JavaTypes.INTEGER() ||
           type == JavaTypes.pINT() ||
           type == JavaTypes.LONG() ||
           type == JavaTypes.pLONG() ||
           type == JavaTypes.SHORT() ||
           type == JavaTypes.pSHORT() ||
           type == JavaTypes.BYTE() ||
           type == JavaTypes.pBYTE();
  }
}
