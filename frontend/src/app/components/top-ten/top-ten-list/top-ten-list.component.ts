import { Component, Input } from '@angular/core';
import {EventTicketCount} from '../../../dtos/topTen/topTen';

@Component({
  selector: 'app-top-ten-list',
  templateUrl: './top-ten-list.component.html',
  styleUrls: ['./top-ten-list.component.scss']
})

export class TopTenListComponent {
  @Input() topTenList: EventTicketCount[];
}
