using System;
using System.Windows;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Căn chỉnh độ rộng label của tất cả text entry control trong panel bằng nhau
    /// </summary>
    public class TextEntryWidthMatcherProperty: BaseAttachedProperty<TextEntryWidthMatcherProperty, string>
    {
        public override void OnValueChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
        {
            // Lấy panel (thông thường là grid)
            var panel = (d as Panel);

            // Gọi SetWidths (giúp hiển thị được ở cả design time)
            SetWidths(panel);

            RoutedEventHandler onLoaded = null;
            onLoaded = (s, ee) =>
            {
                // Unhook
                panel.Loaded -= onLoaded;

                // Set widths
                SetWidths(panel);

                // Lặp qua từng phần con khác trong panel
                foreach(var children in panel.Children)
                {
                    // Bỏ qua những cái không phải là text entry control
                    if (!(children is TextEntryControl control)){
                        continue;
                    }

                    control.Label.SizeChanged += (ss, eee) =>
                    {
                        // Update lại độ rộng của toàn bộ label của text entry control có trong panel
                        SetWidths(panel);
                    };
                }
            };

            // Hook đến loaded event
            panel.Loaded += onLoaded;
        }

        /// <summary>
        /// Update toàn bộ độ rộng label của text entry control có trong panel này bằng độ rộng của cái lớn nhất
        /// </summary>
        /// <param name="panel"></param>
        public void SetWidths(Panel panel)
        {
            var maxSize = 0d;

            foreach(var child in panel.Children)
            {
                // Bỏ qua những thứ không phải là text entry control
                if(!(child is TextEntryControl control))
                {
                    continue;
                }

                // Tìm ra độ rộng lớn nhất trong đám
                maxSize = Math.Max(maxSize, control.Label.RenderSize.Width + control.Label.Margin.Left + control.Label.Margin.Right);
            }

            var gridLength = (GridLength)new GridLengthConverter().ConvertFromString(maxSize.ToString());

            foreach(var child in panel.Children)
            {
                // Bỏ qua những thứ không phải là text entry control
                if (!(child is TextEntryControl control))
                {
                    continue;
                }

                // Set độ rộng label của text entry control trong panel bằng với độ rộng lớn nhất tìm được
                control.LabelWidth = gridLength;
            }
        }

    }
}
