package gw.lang.parser.exceptions;

/**
 */
public interface IWarningSuppressor
{
  boolean isSuppressed( String warningCode );
}
