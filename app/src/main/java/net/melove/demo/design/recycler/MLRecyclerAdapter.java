package net.melove.demo.design.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import net.melove.demo.design.R;

/**
 * Created by lzan13 on 2016/11/24.
 */
public class MLRecyclerAdapter extends RecyclerView.Adapter<MLRecyclerAdapter.RecyclerViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;

    private List<String> mList;

    public MLRecyclerAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        if (position % 10 == 0) {
            holder.iconView.setVisibility(View.VISIBLE);
        } else {
            holder.iconView.setVisibility(View.GONE);
        }

        String str = mList.get(position);
        holder.contentView.setText(str);
    }

    @Override public int getItemCount() {
        return mList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView iconView;
        public TextView contentView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.ml_img_icon);
            contentView = (TextView) itemView.findViewById(R.id.ml_text_content);
        }
    }
}
