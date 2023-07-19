package com.ifpe.criptoscan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CustomDialog extends AlertDialog {

    private RadioGroup radioGroup;
    private EditText inputNumber;

    protected CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar o layout customizado para o diálogo
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        setView(dialogView);

        // Obter referências para os componentes do diálogo
        radioGroup = dialogView.findViewById(R.id.radio_group);
        inputNumber = dialogView.findViewById(R.id.input_number);

        // Definir um botão positivo para o diálogo
        setButton(DialogInterface.BUTTON_POSITIVE, "OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica a ser executada quando o botão OK for clicado
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String radioButtonValue = selectedRadioButton.getText().toString();

                String inputNumberValue = inputNumber.getText().toString();

                // Realize as operações necessárias com os valores selecionados e inseridos

                dismiss(); // Fechar o diálogo
            }
        });

        // Definir um botão negativo para o diálogo
        setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss(); // Fechar o diálogo sem executar nenhuma ação
            }
        });
    }
}

