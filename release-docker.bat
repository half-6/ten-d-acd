"D:\tools\winscp517\pscp" -v -pw bbh2gd "D:\codes\qyotech\ten-d-acd\backend\target\com.tend.acd.backend-0.0.8-SNAPSHOT.jar" "cyokin@192.168.201.110:/mnt/tend/tend/com.tend.acd.backend.jar"
"D:\tools\winscp517\plink" -v -ssh cyokin@192.168.201.110 -pw bbh2gd "cd /mnt/tend/; sh release.sh"
