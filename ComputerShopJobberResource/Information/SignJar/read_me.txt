1. Make sure that you have a Java SDK keytool and jarsigner in your path. These tools are located in the Java SDK bin directory.

2. Create a new key in a new keystore as follows:

  keytool -genkey -keystore myKeystore -alias myself

You will be prompted for information regarding the new key, such as password, name, etc. This will create the myKeystore file on disk.

3. Then create a self-signed test certificate as follows:

  keytool -selfcert -alias myself -keystore myKeystore

This will prompt you for a password. Generating the certificate may take a few minutes.

4. Check to make sure that everything is okay. To list the contents of the keystore, use this command:

  keytool -list -keystore myKeystore

It should list something like:

  Keystore type: jks
  Keystore provider: SUN


  Your keystore contains 1 entry:
  myself, Tue Jan 23 19:29:32 PST 2001, keyEntry,
  Certificate fingerprint (MD5):
  C2:E9:BF:F9:D3:DF:4C:8F:3C:5F:22:9E:AF:0B:42:9D

5. Finally, sign the JAR file with the test certificate as follows:

  jarsigner -keystore myKeystore test.jar myself

6. Repeat these steps for all your JAR files.

Note that a self-signed test certificate should only be used for internal testing, since it does not guarantee the identity of the user and therefore cannot be trusted. A trustworthy certificate can be obtained from a certificate authority, such as VeriSign orThawte, and should be used when the application is put into production

Make sure you add the following tag to your .jnlp file:

  <security>
    <all-permissions/>
  </security>
