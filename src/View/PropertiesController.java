package View;

import ViewModel.MyViewModel;

/**
 *this class PropertiesController
 * function - setViewModel, Prop
 */

public class PropertiesController {
    public javafx.scene.control.Label lbl_gene;
    public javafx.scene.control.Label lbl_solve;
    public javafx.scene.control.Label lbl_Player;
    public MyViewModel viewModel;

    /**
     * this function setViewModel
     *
     * @param viewModel- the view model we want to set
     */
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * this function Prop - this function sent to the viewModle the category and gets its prop
     * set it in the right label
     */
    public void Prop() {
        String s = viewModel.GetProp("MazeGenerator");
        lbl_gene.setText(s);
        String s1 = viewModel.GetProp("SearchingAlgorithm");
        lbl_solve.setText(s1);
        String s2 = viewModel.GetProp("Player");
        if (s2 != null)
            lbl_Player.setText(s2);
    }
}
