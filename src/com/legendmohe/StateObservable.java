package com.legendmohe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hexinyu on 2019/3/14.
 */
public class StateObservable {

    private int mCurrentState = -1;

    private Map<Object, StateHolder> mHolderMap = new HashMap<Object, StateHolder>();

    private Map<Integer, List<StateHolder>> mStateMap = new HashMap<Integer, List<StateHolder>>();

    public synchronized <T> StateHolder<T> of(T observer) {
        StateHolder<T> stateHolder = new StateHolder<T>(observer);
        mHolderMap.put(observer, stateHolder);
        return stateHolder;
    }

    public synchronized boolean transferTo(int state) {
        mCurrentState = state;
        boolean handled = false;
        List<StateHolder> stateHolders = mStateMap.get(state);
        if (stateHolders != null) {
            for (StateHolder stateHolder : stateHolders) {
                if (stateHolder.mListener != null) {
                    stateHolder.mListener.onStateChanged(stateHolder.mObserver, state);
                }
                handled = true;
            }
        }
        return handled;
    }

    public synchronized <T> void removeObserver(T observer) {
        StateHolder<T> stateHolder = mHolderMap.get(observer);
        if (stateHolder != null) {
            for (List<StateHolder> holderList : stateHolder.mHolderLists) {
                holderList.remove(stateHolder);
            }
            mHolderMap.remove(observer);
        }
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public class StateHolder<T> {
        private T mObserver;
        private StateListener mListener;
        private List<List<StateHolder>> mHolderLists = new ArrayList<List<StateHolder>>();

        private StateHolder(T view) {
            mObserver = view;
        }

        void onStateChanged(StateListener<T> listener) {
            mListener = listener;
        }

        public StateHolder<T> observing(int[] states) {
            for (int state : states) {
                List<StateHolder> stateHolders = mStateMap.get(state);
                if (stateHolders == null) {
                    stateHolders = new ArrayList<StateHolder>();
                    mStateMap.put(state, stateHolders);
                }
                if (!stateHolders.contains(this)) {
                    stateHolders.add(this);
                    mHolderLists.add(stateHolders);
                }
            }
            return this;
        }
    }

    public interface StateListener<T> {
        void onStateChanged(T observer, int state);
    }
}
