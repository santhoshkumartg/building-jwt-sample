### What is this repository for? ###

* This repository has sample java code to generate a JWT string by using HS256 signature algorithm and to obtain a access token by making a http call to the token endpoint of the authorization service.
* Please refer buildJwtString() method in jwt.JwtBuilder class for building JWT string.
* Also refer obtainToken() method in jwt.JwtBuilder class to obtain the token from authorization service by client_secret_jwt authentication method.

* The sample program takes three arguments listed below
    *   Client ID - The client Id that should be used for authentication to obtain the access token
    *   Client Secret - The client secret that should be used for authentication to obtain the access token. This would be provided by the Reltio
    *   Token Endpoint - The token endpoint of authorization service 

* VERSION - 0.1.0




