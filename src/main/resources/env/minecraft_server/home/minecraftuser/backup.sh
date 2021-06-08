echo "BACKUP"
tar -czpf /home/minecraftuser/minecraftdir/backup/server-$(date +%F-%H-%M).tar.gz /home/minecraftuser/minecraftdir/server

## Delete older backups
find /home/minecraftuser/minecraftdir/backup/ -type f -mmin +10 -name '*.gz' -delete
