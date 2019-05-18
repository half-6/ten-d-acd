scp -v -r -i D:\codes\mykey\tend-admin-ec2.pem  "D:\codes\qyotech\ten-d-acd\docker\cloud.yml" "root@47.103.63.195:/mnt/tend/"
ssh -i D:\codes\mykey\tend-admin-ec2.pem root@47.103.63.195 "cd /mnt/tend/; docker-compose -f cloud.yml down --rmi local;docker-compose -f cloud.yml up -d admin"
