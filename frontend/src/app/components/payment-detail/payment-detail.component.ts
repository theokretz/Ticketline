import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {PaymentDetail} from '../../dtos/payment-detail';
import {DialogData} from '../../services/cart.service';

@Component({
  selector: 'app-payment-detail',
  templateUrl: './payment-detail.component.html',
  styleUrls: ['./payment-detail.component.scss']
})
export class PaymentDetailComponent implements OnInit {
  displayedColumns: string[] = ['name', 'number', 'expirationDate', 'cvv'];
  constructor(public dialogRef: MatDialogRef<PaymentDetailComponent>,
              @Inject(MAT_DIALOG_DATA) public dialogData: DialogData) {
  }
  ngOnInit(): void {
  }

  buy() {
    this.dialogRef.close();
  }

  getFormattedExpirationDate(expirationDate: Date) {
    const month = ('0' + (expirationDate.getMonth() + 1)).slice(-2);
    const year = expirationDate.getFullYear().toString().slice(-2);
    return month + ' / ' + year;
  }
}
