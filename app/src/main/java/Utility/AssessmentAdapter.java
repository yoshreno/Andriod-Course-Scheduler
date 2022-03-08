package Utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196_pa.R;

import java.util.List;

import Entity.Assessment;
import UI.AssociateAssessment;
import UI.AssessmentDetail;
import UI.AssessmentList;
import UI.AssociateCourse;
import UI.CourseDetail;
import UI.TermDetail;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView AssessmentItemView;
        private AssessmentViewHolder(View itemView) {
            super(itemView);
            AssessmentItemView = itemView.findViewById(R.id.itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = getAdapterPosition();
                    notifyDataSetChanged();
                    Assessment selectedAssessment = mAssessments.get(selectedIndex);

                    if(context.getClass().equals(AssessmentList.class)) {
                        Intent intent = new Intent(context, AssessmentDetail.class);
                        intent.putExtra("id", selectedAssessment.getId());
                        intent.putExtra("title", selectedAssessment.getTitle());
                        intent.putExtra("startDate", selectedAssessment.getStartDate());
                        intent.putExtra("endDate", selectedAssessment.getEndDate());
                        intent.putExtra("type", selectedAssessment.getType());
                        intent.putExtra("courseID", selectedAssessment.getCourseId());
                        intent.putExtra("index", selectedIndex);
                        context.startActivity(intent);
                    }
                    else if (context.getClass().equals(AssociateAssessment.class)) {
                        selectedAssessment.setCourseId(AssociateAssessment.courseID);
                        AssociateAssessment.assessment = selectedAssessment;
                    }
                }
            });
        }
    }

    private int selectedIndex = -1;
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setAssessments(List<Assessment> Assessments) {
        mAssessments = Assessments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {

        if(context.getClass().equals(CourseDetail.class) || context.getClass().equals(AssociateAssessment.class)) {
            if(selectedIndex == position)
                holder.itemView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            else
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(mAssessments != null) {
            Assessment current = mAssessments.get(position);
            String title = current.getTitle();
            holder.AssessmentItemView.setText("ID: " + current.getId() + " - " + title);
        }
        else
            holder.AssessmentItemView.setText("No assessment has been added. Please add an assessment to view them here.");
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null)
            return mAssessments.size();
        else
            return 0;
    }
}