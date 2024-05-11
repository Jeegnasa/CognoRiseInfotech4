import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button resetButton;
    private Button shuffleButton;

    private List<ImageView> imageViews;
    private List<Integer> imageIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        resetButton = findViewById(R.id.resetButton);
        shuffleButton = findViewById(R.id.shuffleButton);

        // Initialize the list of image views
        imageViews = new ArrayList<>();
        imageIds = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            imageIds.add(getResources().getIdentifier("tile_" + i, "drawable", getPackageName()));
        }

        // Shuffle the image ids
        Collections.shuffle(imageIds);

        // Create image views for each tile
        for (int i = 0; i < 8; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds.get(i));
            imageView.setTag(i + 1); // Tag the image views for later identification
            imageView.setOnClickListener(tileClickListener);
            imageViews.add(imageView);
        }

        // Add image views to the grid layout
        for (ImageView imageView : imageViews) {
            gridLayout.addView(imageView);
        }

        // Set click listeners for buttons
        resetButton.setOnClickListener(resetClickListener);
        shuffleButton.setOnClickListener(shuffleClickListener);
    }

    private View.OnClickListener tileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int currentTag = (int) view.getTag();
            int emptyTag = 8; // The tag of the empty tile

            // Check if the clicked tile is adjacent to the empty tile
            if ((currentTag == emptyTag + 1 && currentTag % 3 != 0) || // Tile to the left
                (currentTag == emptyTag - 1 && emptyTag % 3 != 0) ||   // Tile to the right
                currentTag == emptyTag + 3 ||                          // Tile above
                currentTag == emptyTag - 3) {                          // Tile below

                // Swap the positions of the clicked tile and the empty tile
                ImageView clickedImageView = (ImageView) view;
                ImageView emptyImageView = (ImageView) gridLayout.findViewWithTag(emptyTag);

                BitmapDrawable clickedDrawable = (BitmapDrawable) clickedImageView.getDrawable();
                BitmapDrawable emptyDrawable = (BitmapDrawable) emptyImageView.getDrawable();

                clickedImageView.setImageDrawable(emptyDrawable);
                emptyImageView.setImageDrawable(clickedDrawable);

                clickedImageView.setTag(emptyTag);
                emptyImageView.setTag(currentTag);

                // Check if the puzzle is solved
                if (puzzleSolved()) {
                    Toast.makeText(MainActivity.this, "Puzzle Solved!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private View.OnClickListener resetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Reset the puzzle by shuffling the image ids again
            Collections.shuffle(imageIds);
            for (int i = 0; i < 8; i++) {
                imageViews.get(i).setImageResource(imageIds.get(i));
                imageViews.get(i).setTag(i + 1);
            }
        }
    };

    private View.OnClickListener shuffleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Shuffle the current positions of the tiles
            Collections.shuffle(imageViews);
            gridLayout.removeAllViews();
            for (ImageView imageView : imageViews) {
                gridLayout.addView(imageView);
            }
        }
    };

    private boolean puzzleSolved() {
        // Check if the puzzle is solved
        for (int i = 0; i < 8; i++) {
            if ((int) imageViews.get(i).getTag() != i + 1) {
                return false;
            }
        }
        return true;
    }
}
