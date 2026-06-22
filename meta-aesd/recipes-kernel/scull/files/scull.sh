#!/bin/sh

case "$1" in
    start)
        echo "Loading scull module"
        modprobe scull
        ;;
    stop)
        echo "Unloading scull module"
        modprobe -r scull
        ;;
    *)
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac

exit 0
