package com.example.pocketalert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterInstructionsActivity extends AppCompatActivity {

    private Button prev, next;
    private TextView instruction;
    private int[] instructionIDs;
    private int currentInstruction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_instructions);

        instruction = findViewById(R.id.instructionText);
        next = findViewById(R.id.nextInstructionButton);
        prev = findViewById(R.id.prevInstructionButton);

        // TODO add more instruction. make them different steps.
        instructionIDs = new int[]{R.string.connect_device_instructions_1};

        setInstructionToCurrentInstruction();

        prev.setVisibility(View.INVISIBLE);
        if (currentInstruction == instructionIDs.length - 1) {
            next.setVisibility(View.INVISIBLE);
        }
    }

    public void nextInstruction(View view) {
        if (currentInstruction < instructionIDs.length - 1) {
            if (currentInstruction == 0) {
                prev.setVisibility(View.VISIBLE);
            }

            currentInstruction = currentInstruction + 1;
            setInstructionToCurrentInstruction();

            if (currentInstruction == instructionIDs.length - 1) {
                next.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void previousInstruction(View view) {
        if (currentInstruction > 0) {
            if (currentInstruction == instructionIDs.length - 1) {
                next.setVisibility(View.VISIBLE);
            }

            currentInstruction = currentInstruction - 1;
            setInstructionToCurrentInstruction();

            if (currentInstruction == 0) {
                prev.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setInstructionToCurrentInstruction() {
        instruction.setText(instructionIDs[currentInstruction]);
    }
}