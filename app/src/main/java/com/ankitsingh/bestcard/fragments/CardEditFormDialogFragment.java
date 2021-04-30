package com.ankitsingh.bestcard.fragments;

import android.app.Dialog;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardRewardsAdapter;
import com.ankitsingh.bestcard.adapters.CardsAdapter;
import com.ankitsingh.bestcard.adapters.holders.CardRewardStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.CardType;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.util.CardRewardViewUtil;
import com.ankitsingh.bestcard.util.CardUtil;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import lombok.Builder;

@Builder
public class CardEditFormDialogFragment extends DialogFragment {

    private final BestCardActivity bestCardActivity;
    private final CardsAdapter cardsAdapter;
    private final int position;
    private final Card card;

    private Toolbar toolbar;
    private MenuItem saveButton;
    private ImageView cardIcon;
    private AutoCompleteTextView cardTypeText;
    private TextInputEditText cardAnnualFeesText;
    private TextInputEditText cardMultiplierText;
    private ImageButton cardRewardAdd;
    private RecyclerView cardRewards;

    private CardRewardsAdapter cardRewardsAdapter;

    private List<Reward> originalRewards;

    private final AtomicBoolean cardTypeValid = new AtomicBoolean(true);
    private final AtomicBoolean cardAnnualFeesValid = new AtomicBoolean(true);
    private final AtomicBoolean cardMultiplierValid = new AtomicBoolean(true);
    private final AtomicBoolean cardRewardsValid = new AtomicBoolean(true);

    private final AtomicBoolean cardTypeChanged = new AtomicBoolean(false);
    private final AtomicBoolean cardAnnualFeesChanged = new AtomicBoolean(false);
    private final AtomicBoolean cardMultiplierChanged = new AtomicBoolean(false);
    private final AtomicBoolean cardRewardsChanged = new AtomicBoolean(false);

    private final AtomicBoolean hasChanged = new AtomicBoolean(false);
    private final AtomicBoolean isValid = new AtomicBoolean(false);

    private final View.OnClickListener onDismissClickListener =
            v -> dismiss();

    private final MenuItem.OnMenuItemClickListener onSaveClickListener =
            new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(
                        final MenuItem item) {
                    if (isValid.get() && hasChanged.get()) {
                        final Card updatedCard =
                                Card.builder()
                                        .id(card.getId())
                                        .name(card.getName())
                                        .iconName(card.getIconName())
                                        .cardType(CardType.valueOf(cardTypeText.getText().toString()))
                                        .annualFees(Integer.valueOf(cardAnnualFeesText.getText().toString()))
                                        .multiplier(Double.valueOf(cardMultiplierText.getText().toString()))
                                        .disabled(card.getDisabled())
                                        .rewards(
                                                cardRewardsAdapter.getCurrentCardRewardStateHolders().stream()
                                                        .map(cardRewardStateHolder -> cardRewardStateHolder.getReward().clone())
                                                        .collect(Collectors.toList()))
                                        .build();

                        cardsAdapter.update(
                                position,
                                updatedCard);

                        dismiss();

                        return true;
                    } else {
                        if (!isValid.get()) {
                            Toast.makeText(getActivity(), "Invalid entries", Toast.LENGTH_SHORT).show();
                        } else if (!hasChanged.get()) {
                            Toast.makeText(getActivity(), "Nothing to save", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                }
            };

    private final View.OnClickListener onCardRewardsClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final List<Reward> currentRewards =
                            cardRewardsAdapter.getCurrentCardRewardStateHolders().stream()
                                    .map(CardRewardStateHolder::getReward)
                                    .filter(reward -> !CardUtil.isPastReward(reward))
                                    .collect(Collectors.toList());

                    cardRewardsValid.set(!currentRewards.isEmpty());
                    cardRewardsChanged.set(!CollectionUtils.isEqualCollection(currentRewards, originalRewards));

                    hasCardChanged(
                            cardTypeValid.get(),
                            cardTypeChanged.get(),
                            cardAnnualFeesValid.get(),
                            cardAnnualFeesChanged.get(),
                            cardMultiplierValid.get(),
                            cardMultiplierChanged.get(),
                            cardRewardsValid.get(),
                            cardRewardsChanged.get());
                }
            };

    private final View.OnClickListener onCardRewardsAddClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardRewardViewUtil
                            .createCardRewardAddDialog(
                                    bestCardActivity,
                                    cardRewardsAdapter);
                }
            };

    private final TextWatcher cardTypeTextWatcher =
            new TextWatcher() {
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
                            && card.getCardType().equals(s.toString())) {
                        cardTypeText.setError("Current card type", null);

                        cardTypeValid.set(true);
                        cardTypeChanged.set(false);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    } else if (StringUtils.isBlank(s.toString())) {
                        cardTypeText.setError("Invalid card type", null);

                        cardTypeValid.set(false);
                        cardTypeChanged.set(false);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    } else {
                        cardTypeValid.set(true);
                        cardTypeChanged.set(true);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    }
                }
            };

    private final TextWatcher cardAnnualFeesTextWatcher =
            new TextWatcher() {
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
                            && card.getAnnualFees().equals(Integer.valueOf(s.toString()))) {
                        cardAnnualFeesText.setError("Current annual fees", null);

                        cardAnnualFeesValid.set(true);
                        cardAnnualFeesChanged.set(false);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    } else if (StringUtils.isBlank(s.toString())) {
                        cardAnnualFeesText.setError("Invalid annual fees", null);

                        cardAnnualFeesValid.set(false);
                        cardAnnualFeesChanged.set(false);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    } else {
                        cardAnnualFeesValid.set(true);
                        cardAnnualFeesChanged.set(true);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    }
                }
            };

    private final TextWatcher cardMultiplierTextWatcher =
            new TextWatcher() {
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
                            && card.getMultiplier().equals(Double.valueOf(s.toString()))) {
                        cardMultiplierText.setError("Current multiplier", null);

                        cardMultiplierValid.set(true);
                        cardMultiplierChanged.set(false);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    } else if (StringUtils.isBlank(s.toString())) {
                        cardMultiplierText.setError("Invalid multiplier", null);

                        cardMultiplierValid.set(false);
                        cardMultiplierChanged.set(false);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    } else {
                        cardMultiplierValid.set(true);
                        cardMultiplierChanged.set(true);

                        hasCardChanged(
                                cardTypeValid.get(),
                                cardTypeChanged.get(),
                                cardAnnualFeesValid.get(),
                                cardAnnualFeesChanged.get(),
                                cardMultiplierValid.get(),
                                cardMultiplierChanged.get(),
                                cardRewardsValid.get(),
                                cardRewardsChanged.get());
                    }
                }
            };

    public static CardEditFormDialogFragment display(
            final BestCardActivity bestCardActivity,
            final CardsAdapter cardsAdapter,
            final int position,
            final Card card) {
        final CardEditFormDialogFragment cardEditFormDialogFragment =
                CardEditFormDialogFragment.builder()
                        .bestCardActivity(bestCardActivity)
                        .cardsAdapter(cardsAdapter)
                        .position(position)
                        .card(card)
                        .build();

        cardEditFormDialogFragment.show(bestCardActivity.getSupportFragmentManager(), card.getId());

        return cardEditFormDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullscreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();

        final Dialog dialog = getDialog();

        if (Objects.nonNull(dialog)) {
            final Window window = dialog.getWindow();

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.dialog_form_edit_card, container, false);

        this.toolbar = view.findViewById(R.id.card_form_toolbar);
        this.cardIcon = view.findViewById(R.id.card_icon);
        this.cardTypeText = view.findViewById(R.id.card_form_card_type);
        this.cardAnnualFeesText = view.findViewById(R.id.card_form_annual_fees);
        this.cardMultiplierText = view.findViewById(R.id.card_form_multiplier);
        this.cardRewardAdd = view.findViewById(R.id.card_reward_add);
        this.cardRewards = view.findViewById(R.id.list_card_rewards);

        return view;
    }

    @Override
    public void onViewCreated(
            final View view,
            final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolbar();
        setCardIcon();
        setCardType();
        setCardAnnualFees();
        setCardMultiplier();
        setCardRewards();
    }

    private void setToolbar() {
        this.toolbar.setNavigationOnClickListener(this.onDismissClickListener);

        this.toolbar.setTitle(
                Html.fromHtml(
                        "Edit <b>" + this.card.getName() + "</b>?",
                        Html.FROM_HTML_MODE_COMPACT));

        this.saveButton = this.toolbar.getMenu().findItem(R.id.save);
        this.saveButton.setOnMenuItemClickListener(this.onSaveClickListener);
        this.saveButton.setEnabled(false);
    }

    private void setCardIcon() {
        this.cardIcon.setImageDrawable(getCardIconDrawable());
        this.cardIcon.setOnClickListener(v -> Toast.makeText(getActivity(), "Not supported yet", Toast.LENGTH_SHORT).show());
    }

    private LayerDrawable getCardIconDrawable() {
        final LayerDrawable cardIcon =
                (LayerDrawable) this.bestCardActivity.getDrawable(
                        this.bestCardActivity
                                .getResources()
                                .getIdentifier(
                                        "edit_card",
                                        "drawable",
                                        "com.ankitsingh.bestcard"));

        cardIcon.setDrawableByLayerId(
                R.id.card_icon,
                this.bestCardActivity.getDrawable(
                        this.bestCardActivity
                                .getResources()
                                .getIdentifier(
                                        this.card.getIconName().toLowerCase(),
                                        "drawable",
                                        "com.ankitsingh.bestcard")));

        final VectorDrawable cardIconEditMask = (VectorDrawable) cardIcon.findDrawableByLayerId(R.id.card_icon_edit);
        cardIconEditMask.setAlpha(150);

        return cardIcon;
    }

    private void setCardType() {
        final ArrayAdapter<CardType> cardTypeAdapter =
                new ArrayAdapter<>(
                        this.getContext(),
                        R.layout.form_card_card_type,
                        CardType.values());

        this.cardTypeText.setAdapter(cardTypeAdapter);
        this.cardTypeText.setText(card.getCardType().toString(), false);
        this.cardTypeText.addTextChangedListener(this.cardTypeTextWatcher);
    }

    private void setCardAnnualFees() {
        this.cardAnnualFeesText.setText(String.valueOf(card.getAnnualFees()));
        this.cardAnnualFeesText.addTextChangedListener(this.cardAnnualFeesTextWatcher);
    }

    private void setCardMultiplier() {
        this.cardMultiplierText.setText(String.valueOf(card.getMultiplier()));
        this.cardMultiplierText.addTextChangedListener(this.cardMultiplierTextWatcher);
    }

    private void setCardRewards() {
        this.originalRewards =
                this.card.getRewards().stream()
                        .map(Reward::clone)
                        .filter(reward -> !CardUtil.isPastReward(reward))
                        .collect(Collectors.toList());

        this.cardRewardsAdapter =
                new CardRewardsAdapter(
                        this.cardRewards,
                        this.bestCardActivity,
                        this.card.getRewards().stream()
                                .map(reward -> CardRewardStateHolder.builder().reward(reward.clone()).build())
                                .collect(Collectors.toList()));

        this.cardRewards.setAdapter(cardRewardsAdapter);
        this.cardRewards.setLayoutManager(new LinearLayoutManager(bestCardActivity));
        this.cardRewards.setOnClickListener(this.onCardRewardsClickListener);

        this.cardRewardAdd.setOnClickListener(this.onCardRewardsAddClickListener);
    }

    private void hasCardChanged(
            final boolean cardTypeValid,
            final boolean cardTypeChanged,
            final boolean cardAnnualFeesValid,
            final boolean cardAnnualFeesChanged,
            final boolean cardMultiplierValid,
            final boolean cardMultiplierChanged,
            final boolean cardRewardsValid,
            final boolean cardRewardsChanged) {
        if (cardTypeValid
                && cardAnnualFeesValid
                && cardMultiplierValid
                && cardRewardsValid) {
            this.isValid.set(true);
        } else {
            this.isValid.set(false);
        }

        if (cardTypeChanged
                || cardAnnualFeesChanged
                || cardMultiplierChanged
                || cardRewardsChanged) {
            this.hasChanged.set(true);
        } else {
            this.hasChanged.set(false);
        }

        if (isValid.get() && hasChanged.get()) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }
}
