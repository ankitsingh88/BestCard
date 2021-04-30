package com.ankitsingh.bestcard.fragments;

import android.app.Dialog;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.text.Editable;
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
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.util.CardRewardViewUtil;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.DataUtil;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import lombok.Builder;

@Builder
public class CardAddFormDialogFragment extends DialogFragment {

    private final BestCardActivity bestCardActivity;
    private final CardsAdapter cardsAdapter;

    private Toolbar toolbar;
    private MenuItem saveButton;
    private ImageView cardIcon;
    private TextInputEditText cardNameText;
    private AutoCompleteTextView cardTypeText;
    private TextInputEditText cardAnnualFeesText;
    private TextInputEditText cardMultiplierText;
    private ImageButton cardRewardAdd;
    private RecyclerView cardRewards;

    private CardRewardsAdapter cardRewardsAdapter;

    private final AtomicBoolean cardNameValid = new AtomicBoolean(false);
    private final AtomicBoolean cardTypeValid = new AtomicBoolean(false);
    private final AtomicBoolean cardAnnualFeesValid = new AtomicBoolean(false);
    private final AtomicBoolean cardMultiplierValid = new AtomicBoolean(false);
    private final AtomicBoolean cardRewardsValid = new AtomicBoolean(false);

    private final AtomicBoolean isValid = new AtomicBoolean(false);

    private final View.OnClickListener onDismissClickListener =
            v -> dismiss();

    private final MenuItem.OnMenuItemClickListener onSaveClickListener =
            new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(
                        final MenuItem item) {
                    if (isValid.get()) {
                        final Card card =
                                Card.builder()
                                        .id(UUID.randomUUID().toString())
                                        .name(cardNameText.getText().toString())
                                        .iconName("custom_card")
                                        .cardType(CardType.valueOf(cardTypeText.getText().toString()))
                                        .annualFees(Integer.valueOf(cardAnnualFeesText.getText().toString()))
                                        .multiplier(Double.valueOf(cardMultiplierText.getText().toString()))
                                        .disabled(false)
                                        .rewards(
                                                cardRewardsAdapter.getCurrentCardRewardStateHolders().stream()
                                                        .map(cardRewardStateHolder -> cardRewardStateHolder.getReward().clone())
                                                        .collect(Collectors.toList()))
                                        .build();

                        cardsAdapter.add(
                                card);

                        final List<Category> categoriesToAdd =
                                card.getRewards().stream()
                                        .map(reward -> DataUtil.getCategory(reward.getCategoryId()))
                                        .collect(Collectors.toList());

                        bestCardActivity
                                .getDataLoader()
                                .saveCategories(categoriesToAdd);

                        if (Objects.nonNull(bestCardActivity.getCategoriesFragment().getCategoriesAdapter())) {
                            final Set<String> categoryIdsToAdd =
                                    categoriesToAdd.stream()
                                            .map(Category::getId).collect(Collectors.toSet());

                            bestCardActivity.getCategoriesFragment().getCategoriesAdapter().addCategories(categoryIdsToAdd);
                        }

                        bestCardActivity.setCardsFragment();

                        dismiss();

                        return true;
                    } else {
                        if (!isValid.get()) {
                            Toast.makeText(getActivity(), "Invalid entries", Toast.LENGTH_SHORT).show();
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

                    hasCardChanged(
                            cardNameValid.get(),
                            cardTypeValid.get(),
                            cardAnnualFeesValid.get(),
                            cardMultiplierValid.get(),
                            cardRewardsValid.get());
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

    private final TextWatcher cardNameTextWatcher =
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
                    if (StringUtils.isBlank(s.toString())) {
                        cardNameText.setError("Invalid card name", null);

                        cardNameValid.set(false);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
                    } else {
                        cardNameValid.set(true);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
                    }
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
                    if (StringUtils.isBlank(s.toString())) {
                        cardTypeText.setError("Invalid card type", null);

                        cardTypeValid.set(false);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
                    } else {
                        cardTypeValid.set(true);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
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
                    if (StringUtils.isBlank(s.toString())) {
                        cardAnnualFeesText.setError("Invalid annual fees", null);

                        cardAnnualFeesValid.set(false);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
                    } else {
                        cardAnnualFeesValid.set(true);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
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
                    if (StringUtils.isBlank(s.toString())) {
                        cardMultiplierText.setError("Invalid multiplier", null);

                        cardMultiplierValid.set(false);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
                    } else {
                        cardMultiplierValid.set(true);

                        hasCardChanged(
                                cardNameValid.get(),
                                cardTypeValid.get(),
                                cardAnnualFeesValid.get(),
                                cardMultiplierValid.get(),
                                cardRewardsValid.get());
                    }
                }
            };

    public static CardAddFormDialogFragment display(
            final BestCardActivity bestCardActivity,
            final CardsAdapter cardsAdapter) {
        final CardAddFormDialogFragment cardEditFormDialogFragment =
                CardAddFormDialogFragment.builder()
                        .bestCardActivity(bestCardActivity)
                        .cardsAdapter(cardsAdapter)
                        .build();

        cardEditFormDialogFragment.show(bestCardActivity.getSupportFragmentManager(), "cardAdd");

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

        final View view = inflater.inflate(R.layout.dialog_form_add_card, container, false);

        this.toolbar = view.findViewById(R.id.card_form_toolbar);
        this.cardIcon = view.findViewById(R.id.card_icon);
        this.cardNameText = view.findViewById(R.id.card_form_card_name);
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
        setCardName();
        setCardType();
        setCardAnnualFees();
        setCardMultiplier();
        setCardRewards();
    }

    private void setToolbar() {
        this.toolbar.setNavigationOnClickListener(this.onDismissClickListener);

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
                                        "add_card",
                                        "drawable",
                                        "com.ankitsingh.bestcard"));

        final VectorDrawable cardIconEditMask = (VectorDrawable) cardIcon.findDrawableByLayerId(R.id.card_icon_add);
        cardIconEditMask.setAlpha(150);

        return cardIcon;
    }

    private void setCardName() {
        this.cardNameText.addTextChangedListener(this.cardNameTextWatcher);
    }

    private void setCardType() {
        final ArrayAdapter<CardType> cardTypeAdapter =
                new ArrayAdapter<>(
                        this.getContext(),
                        R.layout.form_card_card_type,
                        CardType.values());

        this.cardTypeText.setAdapter(cardTypeAdapter);
        this.cardTypeText.addTextChangedListener(this.cardTypeTextWatcher);
    }

    private void setCardAnnualFees() {
        this.cardAnnualFeesText.addTextChangedListener(this.cardAnnualFeesTextWatcher);
    }

    private void setCardMultiplier() {
        this.cardMultiplierText.addTextChangedListener(this.cardMultiplierTextWatcher);
    }

    private void setCardRewards() {
        this.cardRewardsAdapter =
                new CardRewardsAdapter(
                        this.cardRewards,
                        this.bestCardActivity,
                        new ArrayList<>());

        this.cardRewards.setAdapter(cardRewardsAdapter);
        this.cardRewards.setLayoutManager(new LinearLayoutManager(bestCardActivity));
        this.cardRewards.setOnClickListener(this.onCardRewardsClickListener);

        this.cardRewardAdd.setOnClickListener(this.onCardRewardsAddClickListener);
    }

    private void hasCardChanged(
            final boolean cardNameValid,
            final boolean cardTypeValid,
            final boolean cardAnnualFeesValid,
            final boolean cardMultiplierValid,
            final boolean cardRewardsValid) {
        if (cardNameValid
                && cardTypeValid
                && cardAnnualFeesValid
                && cardMultiplierValid
                && cardRewardsValid) {
            this.isValid.set(true);
        } else {
            this.isValid.set(false);
        }

        if (isValid.get()) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }
}
