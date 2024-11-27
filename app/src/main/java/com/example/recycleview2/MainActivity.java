import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText nameInput, numberInput;
    private Button addButton;

    private List<Contact> contactList;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        nameInput = findViewById(R.id.nameInput);
        numberInput = findViewById(R.id.numberInput);
        addButton = findViewById(R.id.addButton);

        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList, new ContactAdapter.OnContactActionListener() {
            @Override
            public void onEdit(Contact contact) {
                showEditDialog(contact);
            }

            @Override
            public void onDelete(Contact contact) {
                contactList.remove(contact);
                contactAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String number = numberInput.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                contactList.add(new Contact(name, number));
                contactAdapter.notifyDataSetChanged();
                nameInput.setText("");
                numberInput.setText("");
            }
        });
    }

    private void showEditDialog(Contact contact) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_contact, null);
        EditText editNameInput = dialogView.findViewById(R.id.editNameInput);
        EditText editNumberInput = dialogView.findViewById(R.id.editNumberInput);

        editNameInput.setText(contact.getName());
        editNumberInput.setText(contact.getNumber());

        new AlertDialog.Builder(this)
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = editNameInput.getText().toString();
                    String newNumber = editNumberInput.getText().toString();

                    if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newNumber)) {
                        contact.setName(newName);
                        contact.setNumber(newNumber);
                        contactAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
