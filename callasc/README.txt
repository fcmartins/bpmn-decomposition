Dependencies
============

Required:

 * `javacup-runtime.jar'
 * `tycocommon.jar'
 * `libcvm-extra.jar'
 
Optional:

 * `junit.jar'


Building
========

1. TycoCommon
-------------

CVS URL:

  :extssh:YOURUSERNAME@cvs.dcc.fc.up.pt:/home/cvsroot/tyco/common

Read `common/README-tycocommon.txt' for information on usage.

Copy 'tycocommon.jar' and 'javacup-runtime.jar' from `common' into
`${basedir}/lib'.

Setup the TycoCommon path:

  echo tycocommon.path = PATH/TO/common > build.properties

2. CVM
------

CVS URL:

  :extssh:YOURUSERNAME@cvs.dcc.fc.up.pt:/home/cvsroot/Callas/software/cvm-java

Read `cvm/README.txt' for information on usage.

Copy 'libcvm-extra.jar' from `cvm' into `${basedir}/lib'.   


3. JUnit (optional)
-------------------

Just copy the JUnit 4 JAR into `${basedir}/lib/junit.jar'.

Usage
=====

  ant -p

Generates
=========

 * `callasc.jar': The Callas compiler.
 * `callas-src.zip': The source code for the Callas compiler.
