package com.ec.managementsystem.moduleView.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.FacturasDetasilResponse;
import com.ec.managementsystem.interfaces.IListenerPackingDetail;

import java.util.List;


public class PackingDetailAdapter extends RecyclerView.Adapter<PackingDetailAdapter.PickingDetailViewHolder> {
    private List<FacturasDetasilResponse> pedidoItems;
    private IListenerPackingDetail listener;

    public IListenerPackingDetail getListenerPicking() {
        return listener;
    }

    public void setListenerPicking(IListenerPackingDetail listenerPicking) {
        this.listener = listenerPicking;
    }

    public PackingDetailAdapter(List<FacturasDetasilResponse> pedidoList) {
        this.pedidoItems = pedidoList;
    }

    @NonNull
    @Override
    public PackingDetailAdapter.PickingDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_picking_detail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PickingDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackingDetailAdapter.PickingDetailViewHolder holder, int position) {
        holder.bind(pedidoItems.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return pedidoItems.size();
    }


    public void setPickingItems(List<FacturasDetasilResponse> assets) {
        this.pedidoItems = assets;
        notifyDataSetChanged();
    }

    public void animateTo(List<FacturasDetasilResponse> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<FacturasDetasilResponse> newModels) {
        for (int i = pedidoItems.size() - 1; i >= 0; i--) {
            final FacturasDetasilResponse model = pedidoItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<FacturasDetasilResponse> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final FacturasDetasilResponse model = newModels.get(i);
            if (!pedidoItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<FacturasDetasilResponse> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final FacturasDetasilResponse model = newModels.get(toPosition);
            final int fromPosition = pedidoItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private FacturasDetasilResponse removeItem(int position) {
        final FacturasDetasilResponse model = pedidoItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, FacturasDetasilResponse model) {
        pedidoItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final FacturasDetasilResponse model = pedidoItems.remove(fromPosition);
        pedidoItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class PickingDetailViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvNumber;
        private TextView tvDetail;
        private TextView tvTalla;
        private TextView tvColor;
        private TextView tvQuantity;
        private ImageView ivAction;
        private ImageView ivRemove;
        private LinearLayout item_container;

        PickingDetailViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvTalla = itemView.findViewById(R.id.tvTalla);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivAction = itemView.findViewById(R.id.ivAction);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            item_container = itemView.findViewById(R.id.item_container);
        }

        void bind(final FacturasDetasilResponse item, final int position, final PickingDetailViewHolder holder) {
            if (item != null && position == 0) {
                holder.tvNumber.setText("No.");
                holder.tvDetail.setText("Detalle");
                holder.tvTalla.setText("Talla");
                holder.tvColor.setText("Color");
                holder.tvQuantity.setText("Cant.");
                holder.ivAction.setVisibility(View.INVISIBLE);
                holder.ivRemove.setVisibility(View.GONE);
            } else {
                if (item != null) {
                    holder.tvNumber.setText(String.valueOf(position));
                    holder.tvDetail.setText(item.getDescription());
                    holder.tvTalla.setText(item.getTalla());
                    holder.tvColor.setText(item.getColor());
                    holder.tvQuantity.setText(String.valueOf(item.getUnidadesTotales()));
                    holder.ivAction.setVisibility(View.VISIBLE);
                    holder.ivRemove.setVisibility(View.GONE);
                    holder.item_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                //listenerPicking.onItemClick(item);
                            }
                        }
                    });

                    holder.ivAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onItemActionClick(item);
                            }
                        }
                    });

                    holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onItemRemoveClick(item);
                            }
                        }
                    });
                    if (item.isFinish()) {
                        holder.ivAction.setImageResource(R.drawable.baseline_done_black_48);
                        holder.ivAction.setForegroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                        holder.ivRemove.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

}
