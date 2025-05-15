# 03u502u502
License3J Tutorial for Baby

## What is License3J?
Read this, https://github.com/verhas/License3j.

## Why should I use license for my application
For Safety.

## Flowchart
This is Flowchart of how you use this license
![Flowchart](https://raw.githubusercontent.com/fadhiilabiyyi/03u502u502/refs/heads/master/public/License3J.png)

### Generate Public and Private Key
1. Run License3J REPL with `java -jar License3jrepl-3.1.5-jar-with-dependencies.jar` command
2. Run this command `generateKeys algorithm=RSA size=1024 format=BINARY public=public.key private=private.key
   `, this command will generate Public and Private Key

### Dump public key into byteArray
1. Run License3J REPL with `java -jar License3jrepl-3.1.5-jar-with-dependencies.jar` command
2. Load the public key, `loadPublicKey fileName`
3. Dump the key into Byte Array, `dumpPublicKey fileName`. this command will return an array byte, put key variable into your code
````
--KEY DIGEST START
byte [] digest = new byte[] {
(byte)0xA1,(byte)0x04,(byte)0x1D,(byte)0x2C,(byte)0xF1,
(byte)0x56,(byte)0xFB,(byte)0x06,(byte)0x43,

... some lines are deleted as actual values are irrelevant ...

(byte)0x98,(byte)0xB6,(byte)0xD9,(byte)0x60,
(byte)0x51,(byte)0x9E,(byte)0xA2
};
---KEY DIGEST END
--KEY START
byte [] key = new byte[] {
(byte)0x30,(byte)0x81,(byte)0x9F,(byte)0x30,
(byte)0x0D,(byte)0x06,(byte)0x09,(byte)0x2A,
(byte)0x86,(byte)0x48,(byte)0x86,(byte)0xF7,
(byte)0x0D,(byte)0x01,(byte)0x01,(byte)0x01,
(byte)0x05,(byte)0x00,(byte)0x03,(byte)0x81,
(byte)0x8D,(byte)0x00,(byte)0x30,(byte)0x81,(byte)0x89,

... some lines are deleted as actual values are irrelevant ...

(byte)0xE3,(byte)0xBB,(byte)0xE3,(byte)0xB1,(byte)0x67,(byte)0xAC,(byte)0x2A,(byte)0x9D,
(byte)0x9D,(byte)0x67,(byte)0xB0,(byte)0x9D,(byte)0x3A,(byte)0xDE,(byte)0x48,(byte)0xA5,
(byte)0x2A,(byte)0xE8,(byte)0xBB,(byte)0xC6,(byte)0xE2,(byte)0x39,(byte)0x0D,(byte)0x41,
(byte)0xDF,(byte)0x76,(byte)0xD0,(byte)0xA7,(byte)0x02,(byte)0x03,(byte)0x01,(byte)0x00,
(byte)0x01
};
---KEY END
````

### Sign License with Private Key
1. Run License3J REPL with `java -jar License3jrepl-3.1.5-jar-with-dependencies.jar` command
2. Load the public key, `loadPrivateKey fileName`
3. Run `newLicense` to generate new License File
   1. Add information to your license like expirationDate, Mac Address, etc
      1. To add the information you can add it with this command `feature fieldName:TYPE=value`
      2. Adding macAddress `feature macAddress:STRING=your-mac-address`
      3. Adding expiredDate `feature expireDate:DATE=YYYY-MM-DD HH:MM:SS`
   2. Sign the key, `sign [digest=SHA-512]`
   3. Save the license, `saveLicense [format=TEXT*|BINARY|BASE64] fileName`

### Run Application (Input License)
Run your Java jar alongside with the license.bin file that already been generated

### Application (Include with Public Key)
Public key that application had is the dump result of public key (hard coded) this is ensured the security of the public.key

### License Validation
For license validation there is 3 steps
1. Validate the signed license using license3j library, if the license is not valid the application will remain started, but you can't access the REST Controller.
2. Validate the machine MAC Address, this will ensure one license can be only used by one machine.
3. Validate the expired date of the license, if the license is expired the application will do the same as invalided license.