import { Component, ViewChild, TemplateRef, Input } from '@angular/core';
import { AppService } from './app.service';
import { ToastrService } from 'ngx-toastr';
import {NgbModal, ModalDismissReasons, NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';
import { TagModel } from 'ngx-chips/core/accessor';
import { Observable } from 'rxjs';
import { of } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'library';

  constructor(private appService: AppService,
              private toastr: ToastrService,
              private modalService: NgbModal) {
                this.modalOptions = {
                  backdrop:'static',
                  backdropClass:'customBackdrop'
                }
              }

  pageTable = {
    limit: 10,
    count: 0,
    offset: 0,
    orderBy: 'order_number',
    sortDirection: 'asc',
    mapPropNameFilterSearch: new Map()
  };

  @Input() bookTitle: string;
  @Input() pageNumber: number;
  @Input() authors: any[];
  @Input() isbn: string;
  @Input() genre: string;
  modalTitle = 'ng-bootstrap-modal-demo';
  closeResult: string;
  modalOptions:NgbModalOptions;
  columnsTable: any[] = [];
  rowsTable: any[] = [];
  @ViewChild('sortableHeaderTemplateInput', { static: true })sortableHeaderTemplateInput: TemplateRef<any>;

  ngOnInit() {
    this.initDatatable();
    this.reloadTable();
  }

  addBook(): void {
    let bodyData = [{
      "authors": this.authors,
      "pageNumber": this.pageNumber,
      "genre": this.genre,
      "isbn": this.isbn,
      "title": this.title
    }]
    this.appService.addBooks(bodyData).subscribe((res: any) => {
      this.reloadTable();
      this.toastr.info('Success', `Books successfully added`);
    }, error => {
      this.toastr.error('Error', `Error with generate codes ${error}!`);
    });
  }

  initDatatable(): void {
    this.columnsTable = [
      {
        name: 'ID',
        prop: 'id',
        searchName: 'id',
        sortable: true,
        resizeable: false
      },
      {
        name: 'TITLE',
        prop: 'title',
        searchName: 'title',
        sortable: true,
        resizeable: false,
        width: 350
      },
      {
        name: 'GENRE',
        prop: 'genre',
        searchName: 'genre',
        sortable: true,
        resizeable: false
      },
      {
        name: 'AUTHORS',
        prop: 'authors',
        searchName: 'authors',
        sortable: true,
        resizeable: false,
        width: 650
      },
      {
        name: 'PAGENUMBER',
        prop: 'pageNumber',
        searchName: 'pageNumber',
        sortable: true,
        resizeable: false
      },
      {
        name: 'ISBN',
        prop: 'isbn',
        searchName: 'isbn',
        sortable: true,
        resizeable: false
      }
    ];
  }

  reloadTable() {
    this.appService.searchBooks(null).subscribe((res: any) => {
      this.rowsTable = res;
      console.log(res);
      this.pageTable.count =  res.totalDocs;
    });
  }

  pageCallback(
    pageInfo: {
      count?: number,
      pageSize?: number,
      limit?: number,
      offset?: number
    }) {
    this.pageTable.offset = pageInfo.offset;
    this.reloadTable();
  }

  open(content) {
    this.modalService.open(content, this.modalOptions).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

  public onAdding(tag: TagModel): Observable<TagModel> {
    const confirm = window.confirm('Do you really want to add this tag?');
    this.authors.push(tag);
    return of(tag);
    
}

public onAdd(item) {
  console.log('tag added: value is ' + item);
}

public addDefaultBooks(): void {
  let data = [{
    "title": "Don Quixote",
    "authors": ["Miguel de Cervantes"],
    "genre": "Adventure",
    "pageNumber": "600",
    "isbn": "1111111111"
  }, 
  {
    "title": "Tale of Two Cities",
    "authors": ["Charles Dickens"],
    "genre": "Adventure",
    "pageNumber": "569",
    "isbn": "2354879854"
  },
  {
    "title": "Lord of the Rings",
    "authors": ["J.R.R. Tolkien"],
    "genre": "Adventure",
    "pageNumber": "734",
    "isbn": "7863934521"
  },
  {
    "title": "The Lion, the Witch and the Wardrobe",
    "authors": ["C.S. Lewis"],
    "genre": "Child Books",
    "pageNumber": "599",
    "isbn": "1879653425"
  },
  {
    "title": "Little Prince",
    "authors": ["Antoine de Saint-Exupery"],
    "genre": "Child Books",
    "pageNumber": "234",
    "isbn": "7516824689"
  },
  {
    "title": "Harry Potter and the Sorcerer's Stone",
    "authors": ["J.K. Rowling"],
    "genre": "Child Books",
    "pageNumber": "487",
    "isbn": "7534879671"
  },
  {
    "title": "And Then There Were None",
    "authors": ["Agatha Christie"],
    "genre": "Thriller",
    "pageNumber": "368",
    "isbn": "7214968246"
  },
  {
    "title": "The Dream of the Red Table",
    "authors": ["Cao Xueqin"],
    "genre": "Drama",
    "pageNumber": "533",
    "isbn": "4689534125"
  },
  {
    "title": "The Hobbit",
    "authors": ["J.R.R Tolkien"],
    "genre": "Sci-Fi",
    "pageNumber": "488",
    "isbn": "5896741235"
  },
  {
    "title": "The Da Vinci Code",
    "authors": ["Dan Brown"],
    "genre": "Thriller",
    "pageNumber": "587",
    "isbn": "5723489672"
  }];
  this.appService.addBooks(data).subscribe((res: any) => {
    this.reloadTable();
    this.toastr.info('Success', `Books successfully added`);
  }, error => {
    this.toastr.error('Error', `Error with generate codes ${error}!`);
  });
}

}

