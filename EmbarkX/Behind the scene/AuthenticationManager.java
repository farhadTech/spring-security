public interface AuthenticationManager {
  /*
  Attempts to authenticate the passed Authentication object, returning a fully populated Authentication object (including granted authorities) if successful.
  An AuthenticationManager must honour the following contract concerning exceptions.

  * A DisabledException must be thrown if an account is disabled and the AuthenticationManager can test for this state.
  * A LockedException must be thrown if an account is locked and the AuthenticationManager can test for account locking.
  * A BadCredentialsException must be thrown if incorrect credentials are presented. Whilst the above exceptions are optional, an AuthenticationManager must alsways test credentials.

  Exceptions should be tested and if applicable thrown in the order expressed above (i.e. if an account is disabled or locked, the authentication request is immediately rejected and the credentials testing process is not performed). This prevents credentials being tested against disabled or locked accounts.

  Params: authentication - the authentication request object
  Returns: a fully authenticated object including credentials.
  Throws: AuthenticationException - if authentication fails.
  */

  Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
