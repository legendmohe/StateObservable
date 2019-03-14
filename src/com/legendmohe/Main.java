package com.legendmohe;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int BUSINESS_STATE_1 = 1;
    public static final int BUSINESS_STATE_2 = 2;

    public static void main(String[] args) {
        // 一系列的view，监听一系列的业务状态变化
        List<Integer> views = new ArrayList<Integer>();
        // 创建一个StateObservable来管理这些view和业务状态
        StateObservable stateObservable = new StateObservable();
        stateObservable.of(views)
                .observing(new int[]{BUSINESS_STATE_1, BUSINESS_STATE_2})
                .onStateChanged(new StateObservable.StateListener<List<Integer>>() {
                    @Override
                    public void onStateChanged(List<Integer> observer, int state) {
                        log("onStateChanged() called with: " + "observer = [" + observer + "], state = [" + state + "]");
                        // 这些view该怎么变化
                        switch (state) {
                            case BUSINESS_STATE_1:
                                // 业务状态1下，view的变化
                                break;
                            case BUSINESS_STATE_2:
                                // 业务状态2下，view的变化
                                break;
                        }
                    }
                });
        // 整体转移到业务状态1
        stateObservable.transferTo(BUSINESS_STATE_1);
        // 整体转移到业务状态2
        stateObservable.transferTo(BUSINESS_STATE_2);
    }

    private static void log(String s) {
        System.out.println(s);
    }
}
