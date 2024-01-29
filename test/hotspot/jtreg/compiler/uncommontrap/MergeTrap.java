/*
 * Copyright Amazon.com Inc. or its affiliates. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package compiler.uncommontrap;

import compiler.lib.ir_framework.*;
import jdk.test.lib.Asserts;

/*
 * @test
 * @bug 8323411
 * @summary Test merging of traps
 * @library /test/lib /
 * @run driver compiler.uncommontrap.MergeTrap
 */
public class MergeTrap {
    int val;
    public MergeTrap() {}
    public MergeTrap(int val) {
        this.val = val;
    }

    public static void main(String[] args) {
        TestFramework.run();
    }

    @DontInline
    private void blackhole() {}

    @DontInline
    private MergeTrap getObj() {
        return new MergeTrap(1);
    }

    @DontInline
    private Object[] getArr() {
        int size = 10;
        Object[] arr = new Object[size];
        for (int i = 0; i < size; ++i) {
            arr[i] = new Object();
        }
        return arr;
    }

    @Test
    @IR(counts = {IRNode.NULL_CHECK_TRAP, "1"})
    @IR(counts = {IRNode.UNSTABLE_FUSED_IF_TRAP, "1"})
    @IR(failOn = {IRNode.RANGE_CHECK_TRAP})
    @IR(failOn = {IRNode.UNSTABLE_IF_TRAP})
    public void arrayStore() {
        Object[] arr = getArr();
        arr[3] = new Object();
    }

    @Test
    @Arguments({Argument.NUMBER_42, Argument.DEFAULT, Argument.DEFAULT, Argument.NUMBER_MINUS_42})
    @IR(counts = {IRNode.UNSTABLE_FUSED_IF_TRAP, "1"})
    @IR(failOn = {IRNode.UNSTABLE_IF_TRAP})
    public void twoCompares(int A, int a, int B, int b) {
        if (A > a && B > b) {
            blackhole();
        }
    }

    @Test
    @IR(counts = {IRNode.NULL_CHECK_TRAP, "1"})
    @IR(counts = {IRNode.RANGE_CHECK_TRAP, "1"})
    @IR(counts = {IRNode.UNSTABLE_IF_TRAP, "1"})
    @IR(failOn = {IRNode.UNSTABLE_FUSED_IF_TRAP})
    public void dontFuseRangeCheck() {
        // We should be able to fuse the range check and unstable if in theory. The load
        // from the array is in between the if-checks and is preventing merging the two
        // if's.
        Object[] arr = getArr();
        if (arr[5] != null) {
            blackhole();
        }
    }

    @Test
    @IR(counts = {IRNode.NULL_CHECK_TRAP, "1"})
    @IR(counts = {IRNode.UNSTABLE_IF_TRAP, "1"})
    @IR(failOn = {IRNode.UNSTABLE_FUSED_IF_TRAP})
    public void dontFuseNullCheck() {
        MergeTrap mt = getObj();
        if (mt.val > 0) {
            blackhole();
        }
    }
}

