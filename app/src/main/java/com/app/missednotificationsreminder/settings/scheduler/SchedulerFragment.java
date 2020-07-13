package com.app.missednotificationsreminder.settings.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.missednotificationsreminder.binding.util.BindableObject;
import com.app.missednotificationsreminder.databinding.FragmentSchedulerBinding;
import com.app.missednotificationsreminder.ui.fragment.common.CommonFragmentWithViewModel;
import com.app.missednotificationsreminder.util.TimeUtils;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import javax.inject.Inject;

import dagger.android.ContributesAndroidInjector;

/**
 * Fragment which displays scheduler settings view
 *
 * @author Eugene Popovich
 */
public class SchedulerFragment extends CommonFragmentWithViewModel<SchedulerViewModel> {

    @Inject SchedulerViewModel model;
    FragmentSchedulerBinding mBinding;

    @Override public SchedulerViewModel getModel() {
        return model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = FragmentSchedulerBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view, savedInstanceState);
    }

    private void init(View view, Bundle savedInstanceState) {
        mBinding.setFragment(this);
        mBinding.setModel(model);
    }

    public void selectTime(
            BindableObject<Integer> minutes, int minMinutes, int maxMinutes) {
        // launch the time picker dialog
        TimePickerDialog tpg = TimePickerDialog.newInstance(
                (view, hourOfDay, minute, second) -> minutes.set(hourOfDay * TimeUtils.MINUTES_IN_HOUR + minute),
                minutes.get() / TimeUtils.MINUTES_IN_HOUR, minutes.get() % TimeUtils.MINUTES_IN_HOUR, true);
        tpg.setMinTime(timepointFromMinutes(minMinutes));
        tpg.setMaxTime(timepointFromMinutes(maxMinutes));
        tpg.dismissOnPause(true);
        tpg.show(getActivity().getSupportFragmentManager(), tpg.getClass().getSimpleName());
    }

    /**
     * Method which is called when the begin input is clicked. It launches the time selection dialog
     *
     * @param v
     */
    public void onBeginClicked(View v) {
        selectTime(model.begin, model.minimum, model.end.get());
    }

    /**
     * Method which is called when the end input is clicked. It launches the time selection dialog
     *
     * @param v
     */
    public void onEndClicked(View v) {
        selectTime(model.end, model.begin.get(), model.maximum);
    }
    /**
     * Get the TimePoint instance for the specified minutes of day value
     *
     * @param minutes the minutes of day
     * @return
     */
    Timepoint timepointFromMinutes(int minutes) {
        return new Timepoint(minutes / TimeUtils.MINUTES_IN_HOUR, minutes % TimeUtils.MINUTES_IN_HOUR, 0);
    }

    @dagger.Module
    public static abstract class Module {
        @ContributesAndroidInjector
        abstract SchedulerFragment contribute();
    }
}