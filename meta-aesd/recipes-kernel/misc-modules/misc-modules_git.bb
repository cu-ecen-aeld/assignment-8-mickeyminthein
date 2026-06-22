LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

inherit module update-rc.d

SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-mickeyminthein.git;protocol=https;branch=main \
           file://0001-include-onely-misc-modules-and-scull.patch \
           file://misc-modules.sh \
           "

PV = "1.0+git${SRCPV}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

INITSCRIPT_NAME = "misc-modules"
INITSCRIPT_PARAMS = "defaults 91"

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

do_compile() {
    oe_runmake -C ${S}/misc-modules \
        KERNELDIR=${STAGING_KERNEL_DIR} \
        M=${S}/misc-modules
}

do_install() {
    # Install the kernel module using the standard module install
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake -C ${STAGING_KERNEL_DIR} \
        M=${S}/misc-modules \
        modules_install \
        INSTALL_MOD_PATH=${D}

    # Install init script
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/misc-modules.sh ${D}${INIT_D_DIR}/misc-modules
}

PACKAGESPLITFUNCS:remove = "split_kernel_module_packages"

PACKAGES = "${PN}-dbg ${PN}"

FILES:${PN} = " \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/* \
    ${sysconfdir}/modules-load.d \
    ${sysconfdir}/modprobe.d \
    ${sysconfdir}/init.d/misc-modules \
    ${sysconfdir}/rc*.d/???misc-modules \
"

FILES:${PN}-dbg = " \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/.debug \
"

RDEPENDS:${PN} = ""
