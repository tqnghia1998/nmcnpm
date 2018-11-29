using System;
using System.Collections.Generic;
using System.Text;

namespace DbModel
{
    /// <summary>
    /// The credentials for API client to get list subject on the server
    /// </summary>
    public class ListSubjectResultApiModel
    {
        public List<ListSubjectItemDTO> ListSubject { get; set; }
    }
}
