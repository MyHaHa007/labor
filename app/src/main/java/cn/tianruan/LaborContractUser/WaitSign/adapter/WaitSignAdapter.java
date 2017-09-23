package cn.tianruan.LaborContractUser.WaitSign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.tianruan.LaborContractUser.R;
import cn.tianruan.LaborContractUser.WaitSign.javabean.waitsignjavabean;

/**
 * Created by Administrator on 2017/7/12.
 */
public class WaitSignAdapter extends RecyclerView.Adapter<WaitSignAdapter.MyViewHolder> {
    private Context mContext;
    private List<waitsignjavabean> mData;
    private LayoutInflater inflater;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WaitSignAdapter(Context mContext) {
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }

    public void setmData(List<waitsignjavabean> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_waitsign_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String contract = mData.get(position).getLdhtTemplate().getName();
        holder.contract.setText(contract.substring(0,contract.length()-4));
        holder.companyA.setText("公司(甲方)："+mData.get(position).getPartyAName());
        holder.companyALegalPerson.setText("公司(甲方)法定代表人："+mData.get(position).getPartyAuserName());
        holder.nameB.setText("乙方姓名："+mData.get(position).getPartyBUserName());


//        //时间戳转时间
//        Long startLong = new Long(mData.get(position).getContractStartDate());
//        Date startDate = new Date(startLong);
//        String startTime = simpleDateFormat.format(startDate);
//
//        Long endLong = new Long(mData.get(position).getContractEndDate());
//        Date endDate = new Date(endLong);
//        String endTime = simpleDateFormat.format(endDate);
//
//        holder.startTime.setText("合同起始时间："+startTime);
//        holder.endTime.setText("合同结束数据按："+endTime);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contract;
        TextView companyA;
        TextView companyALegalPerson;
        TextView nameB;
//        TextView startTime;
//        TextView endTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            contract= (TextView) itemView.findViewById(R.id.waitsign_item_contract);
            companyA= (TextView) itemView.findViewById(R.id.waitsign_item_companyA);
            companyALegalPerson= (TextView) itemView.findViewById(R.id.waitsign_item_companyALegalPerson);
            nameB= (TextView) itemView.findViewById(R.id.waitsign_item_nameB);

//            startTime= (TextView) itemView.findViewById(R.id.waitsign_item_startTime);
//            endTime= (TextView) itemView.findViewById(R.id.waitsign_item_endTime);
        }
    }
}
