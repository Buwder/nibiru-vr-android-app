#!/bin/sh
PREBUILT=/home/demo/android-ndk-r8/toolchains/arm-linux-androideabi-4.4.3/prebuilt/linux-x86
./configure --target-os=linux \
--arch=arm \
--enable-cross-compile \
--cc=$PREBUILT/bin/arm-linux-androideabi-gcc \
--cross-prefix=$PREBUILT/bin/arm-linux-androideabi- \
--nm=$PREBUILT/bin/arm-linux-androideabi-nm \
--extra-cflags="-fPIC -DANDROID" \
--enable-static \
--enable-shared \
--disable-asm \
--disable-yasm \
--extra-ldflags="-Wl,-T,$PREBUILT/arm-linux-androideabi/lib/ldscripts/armelf_linux_eabi.x -Wl,-rpath-link=/home/demo/android-ndk-r8/platforms/android-8/arch-arm/usr/lib -L/home/demo/android-ndk-r8/platforms/android-8/arch-arm/usr/lib -nostdlib /home/demo/android-ndk-r8/toolchains/arm-linux-androideabi-4.4.3/prebuilt/linux-x86/lib/gcc/arm-linux-androideabi/4.4.3/crtbegin.o /home/demo/android-ndk-r8/toolchains/arm-linux-androideabi-4.4.3/prebuilt/linux-x86/lib/gcc/arm-linux-androideabi/4.4.3/crtend.o -lc -lm -lz -ldl"
###由于这个版本太低不能使用--disable-symver \