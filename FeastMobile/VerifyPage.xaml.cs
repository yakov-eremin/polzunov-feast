using System.Diagnostics;
using MailKit.Net.Smtp;
using MailKit;
using MimeKit;

namespace FeastMobile;

public partial class VerifyPage : ContentPage
{
	public VerifyPage()
	{
		InitializeComponent();
	}

    private void FirstNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        SecondNum.Focus();
    }

    private void SecondNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        ThirdNum.Focus();
    }

    private void ThirdNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        FourthNum.Focus();
    }

    private void FourthNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        //Check verify num

    }

    private void Button_Clicked(object sender, EventArgs e)
    {
        
    }
}