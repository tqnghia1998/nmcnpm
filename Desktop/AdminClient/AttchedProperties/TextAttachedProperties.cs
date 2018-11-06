using System.Windows;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Focus (keyboard focus) this element on load
    /// </summary>
    public class IsFocusProperty : BaseAttachedProperty<IsFocusProperty, bool>
    {
        public override void OnValueChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
        {
            // If we don't have control, return
            if (!(d is Control control))
            {
                return;
            }

            // Focus this control once loaded
            control.Loaded += (s, se) => control.Focus();

        }
    }
}
