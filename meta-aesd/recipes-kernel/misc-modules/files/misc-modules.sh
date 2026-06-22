#!/bin/sh

MODULES_DIR=/lib/modules/$(uname -r)/extra/misc-modules

case "$1" in
    start)
        echo "Loading misc-modules"
        modprobe hello
        modprobe hellop
        modprobe scullsingle
        modprobe jit
        modprobe jiq
        modprobe seq
        modprobe silly
        modprobe sleepy
        modprobe kdatasize
        modprobe kdataalign
        modprobe complete
        # Use insmod for faulty - conflicts with CONFIG_MD_FAULTY builtin
        insmod ${MODULES_DIR}/faulty.ko
        ;;
    stop)
        echo "Unloading misc-modules"
        rmmod faulty
        modprobe -r hello hellop jit jiq seq silly sleepy kdatasize kdataalign complete
        ;;
    *)
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac

exit 0

