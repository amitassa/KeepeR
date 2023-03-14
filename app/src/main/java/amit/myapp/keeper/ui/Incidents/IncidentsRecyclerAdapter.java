package amit.myapp.keeper.ui.Incidents;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.Model.Roles.PermissionManager;
import amit.myapp.keeper.R;

class IncidentViewHolder extends RecyclerView.ViewHolder {

    TextView titleTv;
    TextView contentTv;
    TextView dateTv;
    TextView publisherTv;
    ImageButton deleteBtn;
    ImageButton editBtn;
    ImageView incidentImg;
    IncidentsModel.IncidentListener<Void> deleteIncidentListener;

    protected List<Incident> incidentsList;

    public IncidentViewHolder(@NonNull View itemView, List<Incident> incidentList,
                              IncidentsRecyclerAdapter.OnItemClickListener listener,
                              IncidentsModel.IncidentListener<Void> deleteIncidentListener,
                              MainActivity mainActivity) {
        super(itemView);
        this.incidentsList = incidentList;
        titleTv = itemView.findViewById(R.id.incidentlistrow_title);
        publisherTv = itemView.findViewById(R.id.incidentlistrow_publisher);
        contentTv = itemView.findViewById(R.id.incidentlistrow_content);
        dateTv = itemView.findViewById(R.id.incidentlistrow_date);
        deleteBtn = itemView.findViewById(R.id.incidentlistrow_delete_btn);
        editBtn = itemView.findViewById(R.id.incidentlistrow_edit_btn);
        incidentImg = itemView.findViewById(R.id.incidentlistrow_img);
        this.deleteIncidentListener = deleteIncidentListener;

        deleteBtn.setOnClickListener(view ->{
            int pos = getAdapterPosition();
            Incident incident = incidentList.get(pos);

            if(!PermissionManager.checkIncidentDeletionPermissions(mainActivity.getCurrentUser(), incident)){
                showNoPermissionError();
                return;
            }
            IncidentsModel.instance().deleteIncident(incident, (data) ->
                    deleteIncidentListener.onComplete(null));
        });

        editBtn.setOnClickListener(view ->{
            int pos = getAdapterPosition();
            Incident incident = incidentList.get(pos);

            if(!PermissionManager.checkEditIncidentPermissions(mainActivity.getCurrentUser(), incident)){
                showNoPermissionError();
                return;
            }
            //ToDo: fix this shit
            NavDirections action = amit.myapp.keeper.ui.messages.MessagesFragmentDirections.actionGlobalEditIncidentFragment(incident);
            Navigation.findNavController(itemView).navigate(action);
        });
        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });


    }
    private void showNoPermissionError(){
        Toast.makeText(contentTv.getContext(), "You do not have permissions to do this action", Toast.LENGTH_SHORT).show();
    }

    public void bind(Incident incident, int pos) {
        titleTv.setText(incident.getTitle());
        contentTv.setText(incident.getContent());
        publisherTv.setText(incident.getPublisherName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(incident.getDate() * 1000);
        if (incident.getPhotourl() != null && !incident.getPhotourl().isEmpty()){
            //ToDo: change placeholder
            Picasso.get().load(incident.getPhotourl()).placeholder(R.drawable.gallery_icon).into(incidentImg);
        }
        dateTv.setText(dateString);
    }
}

public class IncidentsRecyclerAdapter extends RecyclerView.Adapter<IncidentViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }
    OnItemClickListener listener;
    LayoutInflater inflater;
    List<Incident> incidentsList = new LinkedList<Incident>();
    MainActivity mainActivity;

    IncidentsModel.IncidentListener<Void> deleteIncidentListener;

    public void setData(List<Incident> list){
        this.incidentsList = list;
        notifyDataSetChanged();
    }

    public IncidentsRecyclerAdapter(LayoutInflater inflater, List<Incident> list,
                                   IncidentsModel.IncidentListener<Void> deleteListener, MainActivity mainActivity){
        this.inflater = inflater;
        this.incidentsList = list;
        this.deleteIncidentListener = deleteListener;
        this.mainActivity = mainActivity;
    }

    void setOnItemClickListener(OnItemClickListener listener){this.listener = listener;}

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.incident_list_row, parent, false);
        return new IncidentViewHolder(view, incidentsList,listener, deleteIncidentListener, mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position) {
        Incident incident = incidentsList.get(position);
        holder.bind(incident,position);
    }

    @Override
    public int getItemCount() {
        if (incidentsList == null){return 0;}
        return incidentsList.size();
    }

}
