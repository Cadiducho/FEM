FTP_USER=dev
FTP_PASSWORD=plug1ns_123

for file in *.jar; do
    for i in 1 2 3; do
        echo Subiendo $file al servidor $i [137.74.81.20$((i-1))]
        curl --ftp-create-dirs -T $file -u $FTP_USER:$FTP_PASSWORD ftp://137.74.81.20$((i-1))/$file
    done
done

echo Todos los archivos han sido subidos