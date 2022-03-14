#!/bin/sh
java -cp /home/dmitriy/projects/sharelinks/.temp_repo/h2-2.0.206.jar org.h2.tools.RunScript -url jdbc:h2:~/sharelinks -user sa -script sharelinks.zip -options compression zip variable_binary
java -cp ../TJWS2/1.x/webapps/.web-apps-target/bookshelf/WEB-INF/lib/h2-1.4.200.jar org.h2.tools.Script -url jdbc:h2:~/sharelinks -user sa -script sharelinks.zip -options compression zip
