using System;
using System.Windows;

namespace AdminClient
{
    /// <summary>
    /// Attach property cơ bản cho các attach property khác kế thừa
    /// </summary>
    /// <typeparam name="Parent"> Lớp Parent là attach property </typeparam>
    /// <typeparam name="Property"> Kiểu của attach property </typeparam>
    public abstract class BaseAttachedProperty<Parent, Property>
        where Parent: new()
    {
        #region Public Events

        /// <summary>
        /// Kích hoạt khi giá trị thay đổi
        /// </summary>
        public event Action<DependencyObject, DependencyPropertyChangedEventArgs> ValueChanged = (sender, e) => { };

        /// <summary>
        /// Kích hoạt khi giá trị thay đổi, dù cho cùng giá trị
        /// </summary>
        public event Action<DependencyObject, object> ValueUpdated = (sender, value) => { };

        #endregion

        #region Public Properties

        /// <summary>
        /// Một singleton instance của lớp <see cref="Parent"/>
        /// </summary>
        public static Parent Instance { get; private set; } = new Parent();

        #endregion

        #region Attched Properties Definitions

        /// <summary>
        /// Attached property cho class này
        /// </summary>
        public static readonly DependencyProperty ValueProperty = DependencyProperty.RegisterAttached(
            "Value", typeof(Property),
            typeof(BaseAttachedProperty<Parent, Property>),
            new PropertyMetadata(default(Property), new PropertyChangedCallback(OnValuePropertyChanged), new CoerceValueCallback(OnValuePropertyUpdated)));
        
        /// <summary>
        /// Gọi lại event khi <see cref="ValueProperty"/> thay đổi, dù nó có cùng giá trị cũ
        /// </summary>
        /// <param name="d"> UI element mà property của nó thay đổi </param>
        /// <param name="value"> Các đối số của event </param>
        /// <returns></returns>
        private static object OnValuePropertyUpdated(DependencyObject d, object value)
        {
            // gọi function của parent
            (Instance as BaseAttachedProperty<Parent, Property>).OnValueUpdated(d, value);

            // Gọi event listeners
            (Instance as BaseAttachedProperty<Parent, Property>).ValueUpdated(d, value);

            return value;
        }

        /// <summary>
        ///  Gọi lại event khi <see cref="ValueProperty"/> thay đổi
        /// </summary>
        /// <param name="d"> UI element mà property của nó thay đổi </param>
        /// <param name="e"> Các đối số của event </param>
        private static void OnValuePropertyChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
        {
            // Gọi function của parent
            (Instance as BaseAttachedProperty<Parent, Property>).OnValueChanged(d, e);

            // Gọi event listeners
            (Instance as BaseAttachedProperty<Parent, Property>).ValueChanged(d, e);
        }

        /// <summary>
        /// Lấy attach property
        /// </summary>
        /// <param name="d"> Element cần lấy property </param>
        /// <returns></returns>
        public static Property GetValue(DependencyObject d) => (Property)d.GetValue(ValueProperty);

        /// <summary>
        /// Sets attach property
        /// </summary>
        /// <param name="d"> Element cần set property </param>
        /// <param name="value"> Giá trị cần set cho property </param>
        public static void SetValue(DependencyObject d, Property value) => d.SetValue(ValueProperty, value);

        #endregion

        #region Event Methods

        /// <summary>
        /// Method này được gọi khi có attach property nào đó của kiểu này thay đổi, dù cho giá trị mới trùng giá trị cũ
        /// </summary>
        /// <param name="d"> UI element mà property của nó thay đổi </param>
        /// <param name="value"> Các đối số của event </param>
        public virtual void OnValueUpdated(DependencyObject d, object value) { }

        /// <summary>
        /// Method này được gọi khi có attach property nào đó của kiểu này thay đổi
        /// </summary>
        /// <param name="d"> UI element mà property của nó thay đổi </param>
        /// <param name="e"> Các đối số của event </param>
        public virtual void OnValueChanged(DependencyObject d, DependencyPropertyChangedEventArgs e) { }


        #endregion
    }
}
