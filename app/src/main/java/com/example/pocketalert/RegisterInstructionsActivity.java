package com.example.pocketalert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterInstructionsActivity extends AppCompatActivity {

    private Button previous, next;
    private TextView instruction;
    private int[] instructionIDs;
    private int currentInstruction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_instructions);

        instruction = findViewById(R.id.instructionText);
        next = findViewById(R.id.nextInstructionButton);
        previous = findViewById(R.id.prevInstructionButton);

        // TODO add more instruction. make them different steps.
        instructionIDs = new int[]{
                R.string.connect_device_instructions_1,
                R.string.connect_device_instructions_2,
                R.string.connect_device_instructions_3,
                R.string.connect_device_instructions_4};

        setInstructionToCurrentInstruction();

        previous.setVisibility(View.INVISIBLE);
        if (currentInstruction == instructionIDs.length - 1) {
            next.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Goes to the next instruction step if possible.
     */
    public void nextInstruction(View view) {
        if (currentInstruction < instructionIDs.length - 1) {
            currentInstruction = currentInstruction + 1;
            setInstructionToCurrentInstruction();

            // if there are instruction steps before this, show the previous button.
            if (currentInstruction > 0) {
                previous.setVisibility(View.VISIBLE);
            }

            // if this is the last instruction step, hide the next button.
            if (currentInstruction == instructionIDs.length - 1) {
                next.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Goes to the previous instruction step if possible.
     */
    public void previousInstruction(View view) {
        if (currentInstruction > 0) {
            currentInstruction = currentInstruction - 1;
            setInstructionToCurrentInstruction();

            // if there are instruction steps after this, show the next button.
            if (currentInstruction < instructionIDs.length - 1) {
                next.setVisibility(View.VISIBLE);
            }

            // if this is the first instruction step, hide the previous button.
            if (currentInstruction == 0) {
                previous.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Changes the text to the new instruction.
     */
    private void setInstructionToCurrentInstruction() {
        instruction.setText(instructionIDs[currentInstruction]);
    }
}