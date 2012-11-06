/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;
import gw.lang.parser.CaseInsensitiveCharSequence;

public class WrongNumberOfArgsException extends ParseException
{
  public WrongNumberOfArgsException( IParserState standardParserState, ResourceKey msgWrongNumberOfArgsToFunction, CaseInsensitiveCharSequence paramSignature, int expectedArgs, int iArgs) {
    super(standardParserState, msgWrongNumberOfArgsToFunction, paramSignature, expectedArgs, iArgs);
  }
}
