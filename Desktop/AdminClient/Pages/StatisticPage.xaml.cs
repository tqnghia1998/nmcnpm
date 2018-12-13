using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace AdminClient
{
    /// <summary>
    /// Interaction logic for StatisticPage.xaml
    /// </summary>
    public partial class StatisticPage : BasePage<StatisticViewModel>
    {
        #region Constructor

        public StatisticPage(): base()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Constructor with view model
        /// </summary>
        /// <param name="specificViewModel">The specific view model to use for this page</param>
        public StatisticPage(StatisticViewModel specificViewModel = null) : base(specificViewModel)
        {
            InitializeComponent();
        }

        #endregion
    }
}
