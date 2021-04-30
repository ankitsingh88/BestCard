package com.ankitsingh.bestcard.util;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardRewardsAdapter;
import com.ankitsingh.bestcard.adapters.CategoryDropdownAdapter;
import com.ankitsingh.bestcard.adapters.holders.CategoryDropdownStateHolder;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.DefaultInterval;
import com.ankitsingh.bestcard.model.interval.Interval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CardRewardViewUtil {

    public static void createCardRewardAddDialog(
            final AppCompatActivity appCompatActivity,
            final CardRewardsAdapter cardRewardsAdapter) {
        final View view = appCompatActivity.getLayoutInflater().inflate(R.layout.form_reward, null);

        final AutoCompleteTextView categoryText = view.findViewById(R.id.reward_form_category);
        final TextInputEditText cardRewardValueText = view.findViewById(R.id.reward_form_reward_value);

        final SwitchMaterial timeIntervalSelector = view.findViewById(R.id.time_interval_selector);

        final TextInputLayout startDateLayout = view.findViewById(R.id.reward_form_time_interval_start_container);
        final TextInputEditText startDateEditText = view.findViewById(R.id.reward_form_time_interval_start);
        final TextInputLayout endDateLayout = view.findViewById(R.id.reward_form_time_interval_end_container);
        final TextInputEditText endDateEditText = view.findViewById(R.id.reward_form_time_interval_end);

        final AtomicBoolean categoryValid = new AtomicBoolean(false);
        final AtomicBoolean cardRewardValueValid = new AtomicBoolean(false);
        final AtomicBoolean timeIntervalValid = new AtomicBoolean(true);

        final AtomicReference<String> selectedCategoryId = new AtomicReference<>("");

        final CategoryDropdownAdapter categoryDropdownAdapter =
                new CategoryDropdownAdapter(
                        appCompatActivity.getApplicationContext(),
                        R.layout.form_reward_category,
                        DataUtil.getCategories().stream()
                                .map(category -> CategoryDropdownStateHolder.builder().category(category).build())
                                .collect(Collectors.toList()));

        categoryText.setAdapter(categoryDropdownAdapter);
        categoryText.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            final AdapterView<?> parent,
                            final View view,
                            final int position,
                            final long id) {
                        final TextView categoryIdText = view.findViewById(R.id.form_reward_category_id);

                        selectedCategoryId.set(String.valueOf(categoryIdText.getText()));
                    }
                });

        timeIntervalSelector.setChecked(false);

        startDateLayout.setVisibility(View.GONE);
        endDateLayout.setVisibility(View.GONE);

        final AlertDialog alertDialog =
                new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                        .setIcon(R.drawable.edit)
                        .setTitle(
                                Html.fromHtml(
                                        "Add new reward?",
                                        Html.FROM_HTML_MODE_COMPACT))
                        .setView(view)
                        .setPositiveButton(
                                R.string.save,
                                (dialog, id) -> {
                                    final Reward.RewardBuilder updatedRewardBuilder =
                                            Reward.builder()
                                                    .categoryId(selectedCategoryId.get())
                                                    .value(Double.valueOf(cardRewardValueText.getText().toString()));

                                    if (timeIntervalSelector.isChecked()) {
                                        updatedRewardBuilder.interval(
                                                TimeInterval.builder()
                                                        .startDate(isValidDate(startDateEditText.getText().toString()))
                                                        .endDate(isValidDate(endDateEditText.getText().toString()))
                                                        .build());
                                    } else {
                                        updatedRewardBuilder.interval(DefaultInterval.builder().build());
                                    }

                                    cardRewardsAdapter.add(updatedRewardBuilder.build());
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                (dialog, id) -> {})
                        .create();

        final Map<Integer, Button> dialogButtonMap = new HashMap<>();
        alertDialog.setOnShowListener(dialog -> {
            dialogButtonMap.put(
                    AlertDialog.BUTTON_POSITIVE,
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE));

            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        });

        startDateLayout
                .setStartIconOnClickListener(
                        v -> {
                            final MaterialDatePicker.Builder materialDatePickerBuilder =
                                    MaterialDatePicker.Builder.datePicker()
                                            .setTitleText(R.string.start_date);

                            if (Objects.nonNull(isValidDate(startDateEditText.getText().toString()))) {
                                Long timestamp = isValidDate(startDateEditText.getText().toString()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()*1000;
                                timestamp += TimeZone.getDefault().getOffset(timestamp);

                                materialDatePickerBuilder.setSelection(timestamp);
                            }

                            final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

                            materialDatePicker
                                    .addOnPositiveButtonClickListener(
                                            selection -> {
                                                Long timestamp = (Long) selection;
                                                timestamp -= TimeZone.getDefault().getOffset(timestamp);

                                                final LocalDate newStartDate =
                                                        Instant.ofEpochMilli(timestamp)
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDate();

                                                startDateEditText.setText(newStartDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                                            });

                            materialDatePicker.show(appCompatActivity.getSupportFragmentManager(), "newStartDate");
                        });

        endDateLayout
                .setStartIconOnClickListener(
                        v -> {
                            final MaterialDatePicker.Builder materialDatePickerBuilder =
                                    MaterialDatePicker.Builder.datePicker()
                                            .setTitleText(R.string.end_date);

                            if (Objects.nonNull(isValidDate(endDateEditText.getText().toString()))) {
                                Long timestamp = isValidDate(endDateEditText.getText().toString()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()*1000;
                                timestamp += TimeZone.getDefault().getOffset(timestamp);

                                materialDatePickerBuilder.setSelection(timestamp);
                            }

                            final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

                            materialDatePicker
                                    .addOnPositiveButtonClickListener(
                                            selection -> {
                                                Long timestamp = (Long) selection;
                                                timestamp -= TimeZone.getDefault().getOffset(timestamp);

                                                final LocalDate newEndDate =
                                                        Instant.ofEpochMilli(timestamp)
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDate();

                                                endDateEditText.setText(newEndDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                                            });

                            materialDatePicker.show(appCompatActivity.getSupportFragmentManager(), "newEndDate");
                        });

        categoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                if (StringUtils.isBlank(s.toString())) {
                    categoryText.setError("Invalid category", null);

                    categoryValid.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            cardRewardValueValid.get(),
                            timeIntervalValid.get());
                } else {
                    categoryText.setError(null);

                    categoryValid.set(true);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            cardRewardValueValid.get(),
                            timeIntervalValid.get());
                }
            }
        });

        cardRewardValueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                if (StringUtils.isBlank(s.toString())) {
                    cardRewardValueText.setError("Invalid reward value", null);

                    cardRewardValueValid.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            cardRewardValueValid.get(),
                            timeIntervalValid.get());
                } else {
                    cardRewardValueText.setError(null);

                    cardRewardValueValid.set(true);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            cardRewardValueValid.get(),
                            timeIntervalValid.get());
                }
            }
        });

        timeIntervalSelector
                .setOnCheckedChangeListener(
                        (buttonView, isChecked) -> {
                            startDateLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                            endDateLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                            final LocalDate newStartDate = isValidDate(startDateEditText.getText().toString());
                            final LocalDate newEndDate = isValidDate(endDateEditText.getText().toString());

                            timeIntervalValid.set(isValidRange(newStartDate, newEndDate));

                            hasCardRewardChanged(
                                    dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                    categoryValid.get(),
                                    cardRewardValueValid.get(),
                                    timeIntervalValid.get());
                        });

        startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                final LocalDate newStartDate = isValidDate(s.toString());
                final LocalDate newEndDate = isValidDate(endDateEditText.getText().toString());

                if (StringUtils.isBlank(s.toString())) {
                    startDateEditText.setError("Invalid start date", null);

                    timeIntervalValid.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            cardRewardValueValid.get(),
                            timeIntervalValid.get());
                } else {
                    if (Objects.isNull(newStartDate)) {
                        startDateEditText.setError("Invalid start date", null);

                        timeIntervalValid.set(false);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                cardRewardValueValid.get(),
                                timeIntervalValid.get());
                    } else if (!isValidRange(newStartDate, newEndDate)) {
                        startDateEditText.setError("Invalid range", null);

                        timeIntervalValid.set(false);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                cardRewardValueValid.get(),
                                timeIntervalValid.get());
                    } else {
                        startDateEditText.setError(null);
                        endDateEditText.setError(null);

                        timeIntervalValid.set(true);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                cardRewardValueValid.get(),
                                timeIntervalValid.get());
                    }
                }
            }
        });

        endDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                final LocalDate newStartDate = isValidDate(startDateEditText.getText().toString());
                final LocalDate newEndDate = isValidDate(s.toString());

                if (StringUtils.isBlank(s.toString())) {
                    endDateEditText.setError("Invalid end date", null);

                    timeIntervalValid.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            cardRewardValueValid.get(),
                            timeIntervalValid.get());
                } else {
                    if (Objects.isNull(newEndDate)) {
                        endDateEditText.setError("Invalid end date", null);

                        timeIntervalValid.set(false);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                cardRewardValueValid.get(),
                                timeIntervalValid.get());
                    } else if (!isValidRange(newStartDate, newEndDate)) {
                        endDateEditText.setError("Invalid range", null);

                        timeIntervalValid.set(false);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                cardRewardValueValid.get(),
                                timeIntervalValid.get());
                    } else {
                        startDateEditText.setError(null);
                        endDateEditText.setError(null);

                        timeIntervalValid.set(true);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                cardRewardValueValid.get(),
                                timeIntervalValid.get());
                    }
                }
            }
        });

        alertDialog.show();
    }

    public static void createCardRewardEditDialog(
            final AppCompatActivity appCompatActivity,
            final CardRewardsAdapter cardRewardsAdapter,
            final int position,
            final Reward reward) {
        final View view = appCompatActivity.getLayoutInflater().inflate(R.layout.form_reward, null);

        final AutoCompleteTextView categoryText = view.findViewById(R.id.reward_form_category);
        final TextInputEditText cardRewardValueText = view.findViewById(R.id.reward_form_reward_value);

        final SwitchMaterial timeIntervalSelector = view.findViewById(R.id.time_interval_selector);

        final TextInputLayout startDateLayout = view.findViewById(R.id.reward_form_time_interval_start_container);
        final TextInputEditText startDateEditText = view.findViewById(R.id.reward_form_time_interval_start);
        final TextInputLayout endDateLayout = view.findViewById(R.id.reward_form_time_interval_end_container);
        final TextInputEditText endDateEditText = view.findViewById(R.id.reward_form_time_interval_end);

        final AtomicBoolean categoryValid = new AtomicBoolean(true);
        final AtomicBoolean cardRewardValueValid = new AtomicBoolean(true);
        final AtomicBoolean timeIntervalValid = new AtomicBoolean(true);

        final AtomicBoolean categoryChanged = new AtomicBoolean(false);
        final AtomicBoolean cardRewardValueChanged = new AtomicBoolean(false);
        final AtomicBoolean timeIntervalChanged = new AtomicBoolean(false);

        final AtomicReference<String> selectedCategoryId = new AtomicReference<>(reward.getCategoryId());

        final CategoryDropdownAdapter categoryDropdownAdapter =
                new CategoryDropdownAdapter(
                        appCompatActivity.getApplicationContext(),
                        R.layout.form_reward_category,
                        DataUtil.getCategories().stream()
                                .map(category -> CategoryDropdownStateHolder.builder().category(category).build())
                                .collect(Collectors.toList()));

        categoryText.setAdapter(categoryDropdownAdapter);
        categoryText.setText(DataUtil.getCategory(reward.getCategoryId()).getName(), false);
        categoryText.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            final AdapterView<?> parent,
                            final View view,
                            final int position,
                            final long id) {
                        final TextView categoryIdText = view.findViewById(R.id.form_reward_category_id);

                        selectedCategoryId.set(String.valueOf(categoryIdText.getText()));
                    }
                });

        cardRewardValueText.setText(new DecimalFormat("0.##").format(reward.getValue()));

        if (reward.getInterval() instanceof TimeInterval) {
            timeIntervalSelector.setChecked(true);

            startDateLayout.setVisibility(View.VISIBLE);
            endDateLayout.setVisibility(View.VISIBLE);

            final TimeInterval timeInterval = (TimeInterval) reward.getInterval();

            final LocalDate startDate = timeInterval.getStartDate();
            final LocalDate endDate = timeInterval.getEndDate();

            if (Objects.nonNull(startDate)) {
                startDateEditText
                        .setText(startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }

            if (Objects.nonNull(endDate)) {
                endDateEditText
                        .setText(endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        } else {
            timeIntervalSelector.setChecked(false);

            startDateLayout.setVisibility(View.GONE);
            endDateLayout.setVisibility(View.GONE);
        }

        final AlertDialog alertDialog =
                new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                        .setIcon(R.drawable.edit)
                        .setTitle(
                                Html.fromHtml(
                                        new StringBuilder()
                                                .append("Edit <b>")
                                                .append(DataUtil.getCategory(reward.getCategoryId()).getName())
                                                .append("</b>?")
                                                .toString(),
                                        Html.FROM_HTML_MODE_COMPACT))
                        .setView(view)
                        .setPositiveButton(
                                R.string.save,
                                (dialog, id) -> {
                                    final Reward.RewardBuilder updatedRewardBuilder =
                                            Reward.builder()
                                                    .categoryId(selectedCategoryId.get())
                                                    .value(Double.valueOf(cardRewardValueText.getText().toString()))
                                                    .disabled(reward.getDisabled());

                                    if (timeIntervalSelector.isChecked()) {
                                        updatedRewardBuilder.interval(
                                                TimeInterval.builder()
                                                        .startDate(isValidDate(startDateEditText.getText().toString()))
                                                        .endDate(isValidDate(endDateEditText.getText().toString()))
                                                        .build());
                                    } else {
                                        updatedRewardBuilder.interval(DefaultInterval.builder().build());
                                    }

                                    cardRewardsAdapter.update(
                                            position,
                                            updatedRewardBuilder.build());
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                (dialog, id) -> {})
                        .create();

        final Map<Integer, Button> dialogButtonMap = new HashMap<>();
        alertDialog.setOnShowListener(dialog -> {
            dialogButtonMap.put(
                    AlertDialog.BUTTON_POSITIVE,
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE));

            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        });

        startDateLayout
                .setStartIconOnClickListener(
                        v -> {
                            final MaterialDatePicker.Builder materialDatePickerBuilder =
                                    MaterialDatePicker.Builder.datePicker()
                                            .setTitleText(R.string.start_date);

                            if (reward.getInterval() instanceof TimeInterval) {
                                final LocalDate startDate = ((TimeInterval) reward.getInterval()).getStartDate();

                                if(Objects.nonNull(startDate)) {
                                    Long timestamp = startDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()*1000;
                                    timestamp += TimeZone.getDefault().getOffset(timestamp);

                                    materialDatePickerBuilder.setSelection(timestamp);
                                }
                            }

                            if (Objects.nonNull(isValidDate(startDateEditText.getText().toString()))) {
                                Long timestamp = isValidDate(startDateEditText.getText().toString()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()*1000;
                                timestamp += TimeZone.getDefault().getOffset(timestamp);

                                materialDatePickerBuilder.setSelection(timestamp);
                            }

                            final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

                            materialDatePicker
                                    .addOnPositiveButtonClickListener(
                                            selection -> {
                                                Long timestamp = (Long) selection;
                                                timestamp -= TimeZone.getDefault().getOffset(timestamp);

                                                startDateEditText
                                                        .setText(Instant.ofEpochMilli(timestamp)
                                                                        .atZone(ZoneId.systemDefault())
                                                                        .toLocalDate()
                                                                        .format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                                            });

                            materialDatePicker.show(appCompatActivity.getSupportFragmentManager(), reward.getCategoryId());
                        });

        endDateLayout
                .setStartIconOnClickListener(
                        v -> {
                            final MaterialDatePicker.Builder materialDatePickerBuilder =
                                    MaterialDatePicker.Builder.datePicker()
                                            .setTitleText(R.string.end_date);

                            if (reward.getInterval() instanceof TimeInterval) {
                                final LocalDate endDate = ((TimeInterval) reward.getInterval()).getEndDate();

                                if(Objects.nonNull(endDate)) {
                                    Long timestamp = endDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()*1000;
                                    timestamp += TimeZone.getDefault().getOffset(timestamp);

                                    materialDatePickerBuilder.setSelection(timestamp);
                                }
                            }

                            if (Objects.nonNull(isValidDate(endDateEditText.getText().toString()))) {
                                Long timestamp = isValidDate(endDateEditText.getText().toString()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()*1000;
                                timestamp += TimeZone.getDefault().getOffset(timestamp);

                                materialDatePickerBuilder.setSelection(timestamp);
                            }

                            final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

                            materialDatePicker
                                    .addOnPositiveButtonClickListener(
                                            selection -> {
                                                Long timestamp = (Long) selection;
                                                timestamp -= TimeZone.getDefault().getOffset(timestamp);

                                                endDateEditText
                                                        .setText(Instant.ofEpochMilli(timestamp)
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDate()
                                                                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                                            });

                            materialDatePicker.show(appCompatActivity.getSupportFragmentManager(), reward.getCategoryId());
                        });

        categoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                if (StringUtils.isNotBlank(s.toString())
                        && DataUtil.getCategory(reward.getCategoryId()).equals(s.toString())) {
                    categoryText.setError("Current category", null);

                    categoryValid.set(true);
                    categoryChanged.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                } else if (StringUtils.isBlank(s.toString())) {
                    categoryText.setError("Invalid category", null);

                    categoryValid.set(false);
                    categoryChanged.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                } else {
                    categoryText.setError(null);

                    categoryValid.set(true);
                    categoryChanged.set(true);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                }
            }
        });

        cardRewardValueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                if (StringUtils.isNotBlank(s.toString())
                        && reward.getValue().equals(Double.valueOf(s.toString()))) {
                    cardRewardValueText.setError("Current reward value", null);

                    cardRewardValueValid.set(true);
                    cardRewardValueChanged.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                } else if (StringUtils.isBlank(s.toString())) {
                    cardRewardValueText.setError("Invalid reward value", null);

                    cardRewardValueValid.set(false);
                    cardRewardValueChanged.set(false);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                } else {
                    cardRewardValueText.setError(null);

                    cardRewardValueValid.set(true);
                    cardRewardValueChanged.set(true);

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                }
            }
        });

        timeIntervalSelector
                .setOnCheckedChangeListener(
                        (buttonView, isChecked) -> {
                            startDateLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                            endDateLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                            final LocalDate newStartDate = isValidDate(startDateEditText.getText().toString());
                            final LocalDate newEndDate = isValidDate(endDateEditText.getText().toString());

                            timeIntervalValid.set(isValidRange(newStartDate, newEndDate));
                            timeIntervalChanged.set(
                                    hasIntervalChanged(
                                            reward.getInterval(),
                                            timeIntervalSelector.isChecked(),
                                            newStartDate,
                                            newEndDate));

                            hasCardRewardChanged(
                                    dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                    categoryValid.get(),
                                    categoryChanged.get(),
                                    cardRewardValueValid.get(),
                                    cardRewardValueChanged.get(),
                                    timeIntervalValid.get(),
                                    timeIntervalChanged.get());
                        });

        startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                final LocalDate newStartDate = isValidDate(s.toString());
                final LocalDate newEndDate = isValidDate(endDateEditText.getText().toString());

                if (StringUtils.isBlank(s.toString())) {
                    startDateEditText.setError("Invalid start date", null);

                    timeIntervalValid.set(false);
                    timeIntervalChanged.set(
                            hasIntervalChanged(
                                    reward.getInterval(),
                                    timeIntervalSelector.isEnabled(),
                                    newStartDate,
                                    newEndDate));

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                } else {
                    if (Objects.isNull(newStartDate)) {
                        startDateEditText.setError("Invalid start date", null);

                        timeIntervalValid.set(false);
                        timeIntervalChanged.set(
                                hasIntervalChanged(
                                        reward.getInterval(),
                                        timeIntervalSelector.isEnabled(),
                                        newStartDate,
                                        newEndDate));

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    } else if (newStartDate.equals(getStartDate(reward.getInterval()))) {
                        startDateEditText.setError("Current start date", null);

                        timeIntervalValid.set(isValidRange(newStartDate, newEndDate));
                        timeIntervalChanged.set(
                                hasIntervalChanged(
                                        reward.getInterval(),
                                        timeIntervalSelector.isEnabled(),
                                        newStartDate,
                                        newEndDate));

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    } else if (!isValidRange(newStartDate, newEndDate)) {
                        startDateEditText.setError("Invalid range", null);

                        timeIntervalValid.set(false);
                        timeIntervalChanged.set(false);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    } else {
                        startDateEditText.setError(null);
                        endDateEditText.setError(null);

                        timeIntervalValid.set(true);
                        timeIntervalChanged.set(
                                hasIntervalChanged(
                                        reward.getInterval(),
                                        timeIntervalSelector.isEnabled(),
                                        newStartDate,
                                        newEndDate));

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    }
                }
            }
        });

        endDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                final LocalDate newStartDate = isValidDate(startDateEditText.getText().toString());
                final LocalDate newEndDate = isValidDate(s.toString());

                if (StringUtils.isBlank(s.toString())) {
                    endDateEditText.setError("Invalid end date", null);

                    timeIntervalValid.set(false);
                    timeIntervalChanged.set(
                            hasIntervalChanged(
                                    reward.getInterval(),
                                    timeIntervalSelector.isEnabled(),
                                    newStartDate,
                                    newEndDate));

                    hasCardRewardChanged(
                            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                            categoryValid.get(),
                            categoryChanged.get(),
                            cardRewardValueValid.get(),
                            cardRewardValueChanged.get(),
                            timeIntervalValid.get(),
                            timeIntervalChanged.get());
                } else {
                    if (Objects.isNull(newEndDate)) {
                        endDateEditText.setError("Invalid end date", null);

                        timeIntervalValid.set(false);
                        timeIntervalChanged.set(
                                hasIntervalChanged(
                                        reward.getInterval(),
                                        timeIntervalSelector.isEnabled(),
                                        newStartDate,
                                        newEndDate));

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    } else if (newEndDate.equals(getEndDate(reward.getInterval()))) {
                        endDateEditText.setError("Current end date", null);

                        timeIntervalValid.set(isValidRange(newStartDate, newEndDate));
                        timeIntervalChanged.set(
                                hasIntervalChanged(
                                        reward.getInterval(),
                                        timeIntervalSelector.isEnabled(),
                                        newStartDate,
                                        newEndDate));

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    } else if (!isValidRange(newStartDate, newEndDate)) {
                        endDateEditText.setError("Invalid range", null);

                        timeIntervalValid.set(false);
                        timeIntervalChanged.set(false);

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    } else {
                        startDateEditText.setError(null);
                        endDateEditText.setError(null);

                        timeIntervalValid.set(true);
                        timeIntervalChanged.set(
                                hasIntervalChanged(
                                        reward.getInterval(),
                                        timeIntervalSelector.isEnabled(),
                                        newStartDate,
                                        newEndDate));

                        hasCardRewardChanged(
                                dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE),
                                categoryValid.get(),
                                categoryChanged.get(),
                                cardRewardValueValid.get(),
                                cardRewardValueChanged.get(),
                                timeIntervalValid.get(),
                                timeIntervalChanged.get());
                    }
                }
            }
        });

        alertDialog.show();
    }

    private static void hasCardRewardChanged(
            final Button saveButton,
            final boolean categoryValid,
            final boolean cardRewardValueValid,
            final boolean timeIntervalValid) {
        if (categoryValid
                && cardRewardValueValid
                && timeIntervalValid) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    private static void hasCardRewardChanged(
            final Button saveButton,
            final boolean categoryValid,
            final boolean categoryChanged,
            final boolean cardRewardValueValid,
            final boolean cardRewardValueChanged,
            final boolean timeIntervalValid,
            final boolean timeIntervalChanged) {
        if (categoryValid
                && cardRewardValueValid
                && timeIntervalValid
                && (categoryChanged || cardRewardValueChanged || timeIntervalChanged)) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    private static LocalDate isValidDate(
            final String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        } catch (final Exception e) {
            return null;
        }
    }

    private static boolean isValidRange(
            final LocalDate startDate,
            final LocalDate endDate) {
        if (Objects.nonNull(startDate)
                && Objects.nonNull(endDate)
                && startDate.toEpochDay() < endDate.toEpochDay()
                && LocalDate.now().toEpochDay() <= endDate.toEpochDay()) {
            return true;
        }

        return false;
    }

    private static LocalDate getStartDate(
            final Interval interval) {
        if (interval instanceof TimeInterval) {
            return ((TimeInterval) interval).getStartDate();
        } else {
            return null;
        }
    }

    private static LocalDate getEndDate(
            final Interval interval) {
        if (interval instanceof TimeInterval) {
            return ((TimeInterval) interval).getEndDate();
        } else {
            return null;
        }
    }

    private static boolean hasIntervalChanged(
            final Interval interval,
            final boolean timeIntervalSelectorEnabled,
            final LocalDate newStartDate,
            final LocalDate newEndDate) {
        if (interval instanceof DefaultInterval) {
            if (timeIntervalSelectorEnabled) {
                if (Objects.nonNull(newStartDate)
                        && Objects.nonNull(newEndDate)
                        && newStartDate.toEpochDay() < newEndDate.toEpochDay()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (interval instanceof TimeInterval) {
            final TimeInterval timeInterval = (TimeInterval) interval;

            if (timeIntervalSelectorEnabled) {
                if (Objects.nonNull(newStartDate)
                        && Objects.nonNull(newEndDate)
                        && newStartDate.toEpochDay() < newEndDate.toEpochDay()
                        && (!newStartDate.equals(timeInterval.getStartDate()) || !newEndDate.equals(timeInterval.getEndDate()))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    public static void createCardRewardDeleteDialog(
            final AppCompatActivity appCompatActivity,
            final CardRewardsAdapter cardRewardsAdapter,
            final int position,
            final Reward reward) {
        final String formattedInterval = CardViewUtil.getFormattedDescriptiveInterval(reward.getInterval(), false);

        new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                .setIcon(R.drawable.delete)
                .setMessage(
                        Html.fromHtml(
                                new StringBuilder()
                                        .append("Are you sure you want to delete this reward")
                                        .append(StringUtils.isBlank(formattedInterval) ? formattedInterval : " valid from " + formattedInterval)
                                        .append("?")
                                        .toString(),
                                Html.FROM_HTML_MODE_COMPACT))
                .setTitle(
                        Html.fromHtml(
                                new StringBuilder()
                                        .append("Delete <b>")
                                        .append(DataUtil.getCategory(reward.getCategoryId()).getName())
                                        .append("</b> at <b>")
                                        .append(new DecimalFormat("0.##").format(reward.getValue()))
                                        .append(" <span>&#37;</span></b>?")
                                        .toString(),
                                Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(
                        R.string.delete,
                        (dialog, id) -> {
                            cardRewardsAdapter.delete(position);
                        })
                .setNeutralButton(
                        R.string.cancel,
                        (dialog, id) -> {})
                .show();
    }
}
