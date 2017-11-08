sudo docker stop SPRINGBOOTAPI
sudo docker rm SPRINGBOOTAPI
sudo docker run -ti --name SPRINGBOOTAPI -p 8081:8080 -v /vagrant/apps/healthylinkx-api-springboot:/myapp --link MySQLDB:MySQLDB springbootapi /bin/bash
