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

import Entity.Course;
import UI.AssociateCourse;
import UI.CourseDetail;
import UI.CourseList;
import UI.TermDetail;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = getAdapterPosition();
                    notifyDataSetChanged();
                    Course selectedCourse = mCourses.get(selectedIndex);

                    if(context.getClass().equals(CourseList.class)) {
                        Intent intent = new Intent(context, CourseDetail.class);
                        intent.putExtra("id", selectedCourse.getCourseId());
                        intent.putExtra("title", selectedCourse.getTitle());
                        intent.putExtra("startDate", selectedCourse.getStartDate());
                        intent.putExtra("endDate", selectedCourse.getEndDate());
                        intent.putExtra("status", selectedCourse.getStatus());
                        intent.putExtra("instructorName", selectedCourse.getInstructorName());
                        intent.putExtra("instructorEmail", selectedCourse.getInstructorEmail());
                        intent.putExtra("instructorPhone", selectedCourse.getInstructorPhone());
                        intent.putExtra("notes", selectedCourse.getNotes());
                        intent.putExtra("termId", selectedCourse.getTermId());
                        intent.putExtra("index", selectedIndex);
                        context.startActivity(intent);
                    }
                    else if (context.getClass().equals(TermDetail.class)) {
                        selectedCourse.setTermId(0);
                        TermDetail.selectedCourse = selectedCourse;
                    }
                    else if (context.getClass().equals(AssociateCourse.class)) {
                        //Course course = new Course(selectedCourse.getCourseId(), selectedCourse.getTitle(), selectedCourse.getStartDate(), selectedCourse.getEndDate(), selectedCourse.getStatus(), selectedCourse.getInstructorName(),
                        //        selectedCourse.getInstructorEmail(), selectedCourse.getInstructorPhone(), selectedCourse.getNotes(), AssociateCourse.termID);
                        selectedCourse.setTermId(AssociateCourse.termID);
                        AssociateCourse.course = selectedCourse;
                    }
                }
            });
        }
    }

    private int selectedIndex = -1;
    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new CourseAdapter.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {

        if(context.getClass().equals(TermDetail.class) || context.getClass().equals(AssociateCourse.class)) {
            if(selectedIndex == position)
                holder.itemView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
            else
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(mCourses != null) {
            Course current = mCourses.get(position);
            String title = current.getTitle();
            holder.courseItemView.setText("ID: " + current.getCourseId() + " - " + title);
        }
        else
            holder.courseItemView.setText("No Course has been added. Please add a course to view them here.");
    }

    @Override
    public int getItemCount() {
        if (mCourses != null)
            return mCourses.size();
        else
            return 0;
    }
}