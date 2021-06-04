echo "BACKUP"
tar -czpf /home/minecraftuser/backup/server-$(date +%F-%H-%M).tar.gz /home/minecraftuser/minecraftdir

## Delete older backups
find /home/minecraftuser/backup/ -type f -mmin +10 -name '*.gz' -delete
