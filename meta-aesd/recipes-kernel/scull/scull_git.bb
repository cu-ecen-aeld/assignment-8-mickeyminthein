LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-mickeyminthein.git;protocol=https;branch=main \
           file://0001-include-onely-misc-modules-and-scull.patch \
           file://scull.sh \
           "

PV = "1.0+git${SRCPV}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit module update-rc.d

INITSCRIPT_NAME = "scull"
INITSCRIPT_PARAMS = "defaults 90"

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

do_compile() {
    for subdir in misc-modules scull; do
        oe_runmake -C ${S}/${subdir} \
            KERNELDIR=${STAGING_KERNEL_DIR} \
            M=${S}/${subdir}
    done
}

do_install() {
    # Install kernel modules
    for subdir in misc-modules scull; do
        install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/${subdir}
        install -m 0644 ${S}/${subdir}/*.ko \
            ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/${subdir}/
    done

    # Install init script
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/scull.sh ${D}${INIT_D_DIR}/scull
}

# Remove split_kernel_module_packages so it doesn't auto-generate
# kernel-module-* RDEPENDS that can't be satisfied
PACKAGESPLITFUNCS:remove = "split_kernel_module_packages"

PACKAGES = "${PN}-dbg ${PN}"

FILES:${PN} = " \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/scull/* \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/misc-modules/* \
    ${sysconfdir}/modules-load.d \
    ${sysconfdir}/modprobe.d \
    ${sysconfdir}/init.d/scull \
    ${sysconfdir}/rc*.d/???scull \
"

FILES:${PN}-dbg = " \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/scull/.debug/* \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/misc-modules/.debug/* \
"

RDEPENDS:${PN} = ""
KERNEL_MODULE_AUTOLOAD += "scull"