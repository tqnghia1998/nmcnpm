using System.Windows.Controls;
using System.ComponentModel;

namespace AdminClient
{
    // <summary>
    /// The base page for all pages to gain base functionality
    /// </summary>
    public class BasePage : UserControl
    {
        #region Private Properties

        /// <summary>
        /// The View Model associated with this page
        /// </summary>
        private object mViewModel;

        #endregion

        #region Public Properties

        /// <summary>
        /// The View Model associated with this page
        /// </summary>
        public object ViewModelObject
        {
            get
            {
                return mViewModel;
            }

            set
            {
                // If nothing has changed, return
                if (mViewModel == value)
                {
                    return;
                }

                // Update the value
                mViewModel = value;

                // Fire the view model changed
                OnViewModelChanged();

                // Set the data context for this page
                var temp = value;
                this.DataContext = temp;
            }
        }

        #endregion

        #region  Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public BasePage()
        {
            // Don't bother animating in design time
            if (DesignerProperties.GetIsInDesignMode(this))
            {
                return;
            }
        }

        #endregion

        /// <summary>
        /// Fire when the view model changes
        /// </summary>
        protected virtual void OnViewModelChanged()
        {

        }
    }


    /// <summary>
    /// A base page with added ViewModel support
    /// </summary>
    public class BasePage<VM> : BasePage
        where VM : BaseViewModel, new()
    {
        #region Public Properties

        /// <summary>
        /// The view model associated with this page
        /// </summary>
        public VM ViewModel
        {
            get => (VM)ViewModelObject;
            set => ViewModelObject = value;
        }

        #endregion

        #region  Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public BasePage() : base()
        {
            // Create a default view model
            ViewModel = IoC.Get<VM>();
        }

        /// <summary>
        /// Constructor with specific view model
        /// </summary>
        /// <param name="specificViewModel">The specific view model to use, if any</param>
        public BasePage(VM specificedViewModel = null) : base()
        {
            // Set specific view model
            if (specificedViewModel != null)
            {
                ViewModel = specificedViewModel;
            }
            else
            {
                // Create a default view model
                ViewModel = IoC.Get<VM>();
            }

        }

        #endregion  
    }
}
