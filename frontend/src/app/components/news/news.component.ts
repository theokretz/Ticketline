import {ChangeDetectorRef, Component, OnInit, TemplateRef} from '@angular/core';
import {NewsService} from '../../services/news.service';
import {News} from '../../dtos/news';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {UntypedFormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {

  submitted = false;

  currentNews: News;

  carouselItems: News[] = [];

  showOldNews: false;

  private news: News[] = [];


  constructor(private newsService: NewsService,
              private notification: ToastrService,
              private ngbPaginationConfig: NgbPaginationConfig,
              private formBuilder: UntypedFormBuilder,
              private cd: ChangeDetectorRef,
              private authService: AuthService,
              private modalService: NgbModal) {
  }

  ngOnInit() {
    this.loadNews();


  }

  loadCarouselItems() {
    this.carouselItems = [];
    let counter = 0;
    for (const news of this.getNews()) {
      if (news.imagePath != null) {
        this.carouselItems.push(news);
        counter++;
      }
      if(counter === 5) {
        break;
      }
    }
    }




  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  openAddModal(newsAddModal: TemplateRef<any>) {
    this.currentNews = new News();
    this.modalService.open(newsAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Starts form validation and builds a news dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addNews(form) {
    this.submitted = true;
    if (form.valid) {
      this.currentNews.publicationDate = new Date().toISOString();
      this.createNews(this.currentNews);
      this.clearForm();
    }
  }

  getNews(): News[] {
    return this.news;
  }

  /**
   * Loads the specified page of message from the backend
   */
  loadNews() {
    if(this.showOldNews) {
      this.newsService.getAllNews().subscribe({
        next: (news: News[]) => {
          this.news = news;
          this.loadCarouselItems();
        },
        error: error => {
          this.notification.error(error.error.detail);
        }
      });
    }else {
      this.newsService.getNews().subscribe({
        next: (news: News[]) => {
          this.news = news;
          this.loadCarouselItems();
        },
        error: error => {
          this.notification.error(error.error.detail);
        }
      });
    }
  }


  /**
   * Sends news creation request
   *
   * @param news the news which should be created
   */
  private createNews(news: News) {
    this.newsService.createNews(news).subscribe({
        next: () => {
          this.loadNews();
        },
        error: error => {
          this.notification.error(error.error.detail);
        }
      }
    );
  }





  private clearForm() {
    this.currentNews = new News();
    this.submitted = false;
  }

}
