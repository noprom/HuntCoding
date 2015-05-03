package com.huntdreams.coding;

import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseAnnotationActivity{

    @ViewById
    EditText editName;

    @ViewById
    EditText editGlobal;

    @ViewById
    ImageView imageValify;

    @ViewById
    EditText editValify;

    @ViewById
    View captchaLayout;

    @ViewById
    View valifyDivide;

    @ViewById
    View loginButton;

    @ViewById
    TextView textClause;

    @AfterViews
    void init(){
        textClause.setText(Html.fromHtml("点击立即体验，即表示同意<font color=\"#3bbd79\">《coding服务条款》</font>"));

    }

}
