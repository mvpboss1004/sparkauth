# SparkBasicAuthFilter
This is an implementation of [https://blog.cacoveanu.com/2019/2019.02.11.spark_ui_authentication.html](https://blog.cacoveanu.com/2019/2019.02.11.spark_ui_authentication.html). It's a javax servlet filter that enables authentication for the webuis. For more information, see [https://spark.apache.org/docs/latest/security.html](https://spark.apache.org/docs/latest/security.html).

How to use:
 - 1. Check your spark's servlet-api version in $SPARK_HOME/jars, and modify pom.xml. For spark 3.0.0, it's javax.servlet-api-3.1.0.jar.
 - 2. Build: `mvn clean package`
 - 3. Copy target/sparkauth-1.0.jar to $SPARK_HOME/jars of master,worker and driver nodes.
 - 4. Add the following lines to $SPARK_HOME/conf/spark-defaults.conf of master,worker and driver nodes:
```
spark.ui.filters com.cacoveanu.SparkBasicAuthFilter
spark.com.cacoveanu.SparkBasicAuthFilter.param.username your_user
spark.com.cacoveanu.SparkBasicAuthFilter.param.password your_passwd
spark.com.cacoveanu.SparkBasicAuthFilter.param.realm your_realm
```
 - 5. Restart your cluster
 - 6. The following urls will be protected. Using default ports:
   - http://master:8080
   - http://worker:8081
   - http://driver:4040
 - 7. If you want programmatic authentication, for example when using spark restful api, try:
```
from requests import Session, auth
sess = Session()
sess.auth = auth.HTTPBasicAuth(user, passwd)
r = sess.get('http://master:8080')
```
