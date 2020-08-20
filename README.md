# tls

Reviews the top 1M Alexa sites for the CA aspect of the TLS / SSL certificate tp figure out the CA market share. 

Spawns multiple tasks and threads. (Java concurrency, Executors etc.)

Ran on AWS EC2.

# Usage:

$ cat tls1.log  | grep rank| sed -n -e 's/^.*Issuer confirm//p' | sed -n -e 's/\*\*.*$//p' | sort > a
 
ec2-user@ip-10-234-20-108[ 10.234.20.108 ]--/app--
$ vi a

ec2-user@ip-10-234-20-108[ 10.234.20.108 ]--/app--

ec2-user@ip-10-234-20-108[ 10.234.20.108 ]--/app--
$ sort a | uniq --count


$ rm -f tls1.log; java -Dspring.config.location=/app/resources/ -Dlogging.config=/app/resources/logback.xml -jar tls-0.1.jar >/dev/null 2>&1 &
